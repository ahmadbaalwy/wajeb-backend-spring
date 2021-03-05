package com.example.demo.controllers;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import com.example.demo.models.Question;
import com.example.demo.models.Quizz;
import com.example.demo.payload.request.newQuestionRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.payload.response.image;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuizzRepository;
import com.example.demo.specs.QuestionSpecification;
import com.example.demo.specs.SearchCriteria;
import com.example.demo.specs.SearchOperation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/questions")
public class QuestionController {
    
    @Autowired
    QuizzRepository quizzRepository;

    @Autowired
    QuestionRepository questionRepository;

    @GetMapping("/getQuestions")
    public ResponseEntity<List<Question>> getQuestions(@RequestParam Long quizzId)  {
        QuestionSpecification quizzCheck = new QuestionSpecification();
        quizzCheck.add(new SearchCriteria("quizz", quizzId, SearchOperation.EQUAL));
        //List<Question> questions = questionRepository.findAll(quizzCheck);
        List<Question> questions = questionRepository.getQuestionsSortedBySequence(quizzId);
        return ResponseEntity.ok(questions);

    }

    @PostMapping("/addQuestion")
    public ResponseEntity<?> addQuestion(@RequestParam("type") String type,
    @RequestParam("sequence") String sequence,
    @RequestParam("score") String score,
    @RequestParam("text") String text,
    @RequestParam(name="picByte", required=false) MultipartFile file,
    @RequestParam("quizzId") String quizzId
     
     ) throws IOException {

        Quizz quizz = quizzRepository.findById(Long.valueOf(quizzId)).orElseThrow();
        Question question = new Question(
            type,
            Integer.valueOf(sequence),
            Integer.valueOf(score),
            text
        );
        if (file!=null){
            question.setPicByte(file.getBytes());
        }
        question.setQuizz(quizz);
        questionRepository.save(question);
        if (file!=null){
            questionRepository.uploadPhoto(file.getBytes(), question.getId());
        }
        return ResponseEntity.ok(question.getId());
    }

    @DeleteMapping("/deleteQuestion")
    public ResponseEntity<?> deleteQuestion(@RequestParam Long questionId ) {
       
        Question question = questionRepository.findById(questionId).orElseThrow();
        questionRepository.delete(question);
        return ResponseEntity.ok(new MessageResponse("Question Deleted Successfully!"));
    }

    @GetMapping("/editQuestion")
    public Question editQuestion(@RequestParam Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow();
        return question;
    }

    @PostMapping("/editQuestion")
    public ResponseEntity<?> editQuestion(@RequestParam Long questionId, @RequestBody newQuestionRequest newQuestion ) {
       
        Question question = questionRepository.findById(questionId).orElseThrow();
        question.setType(newQuestion.getType());
        question.setSequence(Integer.valueOf(newQuestion.getSequence()));
        question.setScore(Integer.valueOf(newQuestion.getScore()));
        question.setText(newQuestion.getText());
        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Question Edited Successfully!"));
    }

    @GetMapping("/getQuizzId")
    public Long getQuizzId(@RequestParam Long questionId){
        return questionRepository.getQuizzId(questionId);
    }

    @GetMapping("/getQuestionPhoto")
    public ResponseEntity<?> getQuestionPhoto(@RequestParam Long questionId){
        //return ResponseEntity.ok(questionRepository.getQuestionPhoto(questionId));
        image _image = new image("image","png",questionRepository.getQuestionPhoto(questionId));
        return ResponseEntity.ok(_image);
    }

        // compress the image bytes before storing it in the database
        public static byte[] compressBytes(byte[] data) {
            Deflater deflater = new Deflater();
            deflater.setInput(data);
            deflater.finish();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
            byte[] buffer = new byte[1000000];
            while (!deflater.finished()) {
                int count = deflater.deflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            try {
                outputStream.close();
            } catch (IOException e) {
            }
            System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
            return outputStream.toByteArray();
        }

        // uncompress the image bytes before returning it to the angular application
public static byte[] decompressBytes(byte[] data) {
    Inflater inflater = new Inflater();
    inflater.setInput(data);
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
    byte[] buffer = new byte[1000000];
    try {
        while (!inflater.finished()) {
            int count = inflater.inflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        outputStream.close();
    } catch (IOException ioe) {
    } catch (DataFormatException e) {
    }
    return outputStream.toByteArray();
}
    
}
