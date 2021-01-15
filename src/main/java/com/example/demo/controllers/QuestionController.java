package com.example.demo.controllers;

import java.util.List;

import com.example.demo.models.Question;
import com.example.demo.models.Quizz;
import com.example.demo.payload.request.newQuestionRequest;
import com.example.demo.payload.response.MessageResponse;
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
    public ResponseEntity<?> addQuestion(@RequestBody newQuestionRequest newQuestion) {
        Quizz quizz = quizzRepository.findById(newQuestion.getQuizzId()).orElseThrow();
        Question question = new Question(
            newQuestion.getType(),
            newQuestion.getSequence(),
            newQuestion.getScore(),
            newQuestion.getText()
            );
           
        question.setQuizz(quizz);
        questionRepository.save(question);
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
        question.setSequence(newQuestion.getSequence());
        question.setScore(newQuestion.getScore());
        question.setText(newQuestion.getText());
        questionRepository.save(question);
        return ResponseEntity.ok(new MessageResponse("Question Edited Successfully!"));
    }

    @GetMapping("/getQuizzId")
    public Long getQuizzId(@RequestParam Long questionId){
        return questionRepository.getQuizzId(questionId);
    }


    
}
