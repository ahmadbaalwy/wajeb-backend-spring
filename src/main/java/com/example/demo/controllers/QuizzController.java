package com.example.demo.controllers;

import java.io.Console;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.demo.models.ChanceAnswer;
import com.example.demo.models.Classroom;
import com.example.demo.models.Question;
import com.example.demo.models.Quizz;
import com.example.demo.models.QuizzAnswer;
import com.example.demo.payload.request.newQuizzRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.QuizzRepository;
import com.example.demo.repository.QuizzRepository.newQuizzes;
import com.example.demo.specs.QuizzSpecification;
import com.example.demo.specs.SearchCriteria;
import com.example.demo.specs.SearchOperation;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

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
@RequestMapping("/api/quizzes")
public class QuizzController {

    @Autowired 
    ClassroomRepository classroomRepository;

    @Autowired
    QuizzRepository quizzRepository;

    @GetMapping("/getQuizzes")
    public ResponseEntity<List<Quizz>> getQuizzes(@RequestParam Long classroomId)  {
        QuizzSpecification classroomCheck = new QuizzSpecification();
        classroomCheck.add(new SearchCriteria("classroom", classroomId, SearchOperation.EQUAL));
        List<Quizz> quizzes = quizzRepository.findAll(classroomCheck);
        return ResponseEntity.ok(quizzes);

    }

    @PostMapping("/addQuizz")
    public ResponseEntity<?> addQuizz(@RequestBody newQuizzRequest newQuizz) {
        Classroom classroom = classroomRepository.findById(newQuizz.getClassroomId()).orElseThrow();
        Quizz quizz = new Quizz(
            newQuizz.getQuizzName(),
            newQuizz.isActive(),
            newQuizz.getMaxChances(),
            newQuizz.getGrade(),
            newQuizz.isAllowReview()
            );
        quizz.setCreationDate(new Date());
        quizz.setClassroom(classroom);
        quizzRepository.save(quizz);
        return ResponseEntity.ok(new MessageResponse("Quizz registered successfully !"));
    }

    @DeleteMapping("/deleteQuizz")
    public ResponseEntity<?> deleteQuizz(@RequestParam Long quizzId ) {
       
        Quizz quizz = quizzRepository.findById(quizzId).orElseThrow();
        quizzRepository.delete(quizz);
        return ResponseEntity.ok(new MessageResponse("Quizz Deleted Successfully!"));
    }

    @GetMapping("/editQuizz")
    public Quizz editQuizz(@RequestParam Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).orElseThrow();
        return quizz;
    }

    @PostMapping("/editQuizz")
    public ResponseEntity<?> editQuizz(@RequestParam Long quizzId, @RequestBody newQuizzRequest newQuizz ) {
       
        Quizz quizz = quizzRepository.findById(quizzId).orElseThrow();
        quizz.setQuizzName(newQuizz.getQuizzName());
        quizz.setActive(newQuizz.isActive());
        quizz.setMaxChances(newQuizz.getMaxChances());
        quizz.setGrade(newQuizz.getGrade());
        quizz.setAllowReview(newQuizz.isAllowReview());
        quizzRepository.save(quizz);
        return ResponseEntity.ok(new MessageResponse("Quizz Edited Successfully!"));
    }

    @GetMapping("/getClassroomId")
    public Long getClassroomId(@RequestParam Long quizzId){
        return quizzRepository.getClassroomId(quizzId);
    }

    @GetMapping("/getMaxAllowedChances")
    public ResponseEntity<?> getMaxAllowedChances(@RequestParam Long quizzId){
        return ResponseEntity.ok(quizzRepository.getMaxAllowedChances(quizzId));
    }

    @GetMapping("/getMaxScore")
    public ResponseEntity<?> getMaxScore(@RequestParam Long quizzId){
        return ResponseEntity.ok(quizzRepository.getMaxScore(quizzId));
    }

    @GetMapping("/getNewQuizzes")
    public ResponseEntity<?> getNewQuizzes(@RequestParam String token) throws FirebaseAuthException{
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        if (decodedToken.getEmail()==null){
            username = defaultAuth.getUser(decodedToken.getUid()).getPhoneNumber();
        }
        
        List<newQuizzes> newQuizzesList = quizzRepository.getNewQuizzes(username);
        return ResponseEntity.ok(newQuizzesList);
    }

    @PostMapping("/activateQuizz")
    public ResponseEntity<?> activateQuizz(@RequestParam Long quizzId) {
        Quizz selectedQuizz = quizzRepository.findById(quizzId).orElseThrow();
        selectedQuizz.setActive(true);
        quizzRepository.save(selectedQuizz);
        return ResponseEntity.ok(new String("Quizz Activated Successfully"));
    }

    @GetMapping("/reviewQuizz")
    public Quizz reviewQuizz(@RequestParam Long quizzId, @RequestParam Long chanceId) {
        ArrayList<ChanceAnswer> chanceAnswersToDelete = new ArrayList<ChanceAnswer>();
        Quizz quizz = quizzRepository.findById(quizzId).orElseThrow();
        for (Question question : quizz.getQuestions()) {
            for (QuizzAnswer quizzAnswer : question.getQuizzAnswers()) {
                for (ChanceAnswer chanceAnswer : quizzAnswer.getChanceAnswers()) {
                    //System.out.println(chanceAnswer.getChance().getId());
                    if (chanceAnswer.getChance().getId() != chanceId){
                        //quizzAnswer.deleteChanceAnswer(chanceAnswer);
                        chanceAnswersToDelete.add(chanceAnswer);
                    }
                }
                for (ChanceAnswer chanceAnswer : chanceAnswersToDelete) {
                        quizzAnswer.deleteChanceAnswer(chanceAnswer);
                }
            }
        }

        return quizz;
    }

    @GetMapping("/getQuizzSummary")
    public Quizz getQuizzSummary(@RequestParam Long quizzId) {
        Quizz quizz = quizzRepository.findById(quizzId).orElseThrow();
        quizz.setQuestions(null);
        quizz.setChances(null);
        return quizz;
    }
    
}
