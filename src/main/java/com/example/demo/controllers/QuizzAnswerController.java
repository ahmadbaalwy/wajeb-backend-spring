package com.example.demo.controllers;

import java.util.List;

import com.example.demo.models.Question;
import com.example.demo.models.QuizzAnswer;
import com.example.demo.payload.request.newQuestionRequest;
import com.example.demo.payload.request.newQuizzAnswerRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.QuestionRepository;
import com.example.demo.repository.QuizzAnswerRepository;
import com.example.demo.repository.QuizzRepository;
import com.example.demo.specs.QuestionSpecification;
import com.example.demo.specs.QuizzAnswerSpecification;
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
@RequestMapping("/api/quizzAnswers")
public class QuizzAnswerController {
    
    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    QuizzAnswerRepository quizzAnswerRepository ;

    @GetMapping("/getQuizzAnswers")
    public ResponseEntity<List<QuizzAnswer>> getQuizzAnswers(@RequestParam Long questionId)  {
        
        List<QuizzAnswer> quizzAnswers = quizzAnswerRepository.getQuizzAnswersSortedBySequence(questionId);
        return ResponseEntity.ok(quizzAnswers);

    }

    @PostMapping("/addQuizzAnswer")
    public ResponseEntity<?> addQuizzAnswer(@RequestBody newQuizzAnswerRequest newQuizzAnswer) {
        Question question = questionRepository.findById(newQuizzAnswer.getQuestionId()).orElseThrow();
        QuizzAnswer quizzAnswer = new QuizzAnswer(
            newQuizzAnswer.getText(),
            newQuizzAnswer.isCorrect(),
            newQuizzAnswer.getSequence()
            );
           
        quizzAnswer.setQuestion(question);
        quizzAnswerRepository.save(quizzAnswer);
        return ResponseEntity.ok(new MessageResponse("Quizz_Answer registered successfully !"));
    }

    @DeleteMapping("/deleteQuizzAnswer")
    public ResponseEntity<?> deleteQuizzAnswer(@RequestParam Long quizzAnswerId ) {
       
        QuizzAnswer quizzAnswer = quizzAnswerRepository.findById(quizzAnswerId).orElseThrow();
        quizzAnswerRepository.delete(quizzAnswer);
        return ResponseEntity.ok(new MessageResponse("Quizz_Answer Deleted Successfully!"));
    }

    @GetMapping("/editQuizzAnswer")
    public QuizzAnswer editQuizzAnswer(@RequestParam Long quizzAnswerId) {
        QuizzAnswer quizzAnswer = quizzAnswerRepository.findById(quizzAnswerId).orElseThrow();
        return quizzAnswer;
    }

    @PostMapping("/editQuizzAnswer")
    public ResponseEntity<?> editQuizzAnswer(@RequestParam Long quizzAnswerId, @RequestBody newQuizzAnswerRequest newQuizzAnswer ) {
       
        QuizzAnswer quizzAnswer = quizzAnswerRepository.findById(quizzAnswerId).orElseThrow();
        quizzAnswer.setText(newQuizzAnswer.getText());
        quizzAnswer.setCorrect(newQuizzAnswer.isCorrect());
        quizzAnswer.setSequence(newQuizzAnswer.getSequence());
        quizzAnswerRepository.save(quizzAnswer);
        return ResponseEntity.ok(new MessageResponse("Quizz_Answer Edited Successfully!"));
    }

    @GetMapping("/getQuestionId")
    public Long getQuestionId(@RequestParam Long quizzAnswerId){
        return quizzAnswerRepository.getQuestionId(quizzAnswerId);
    }


    
}
