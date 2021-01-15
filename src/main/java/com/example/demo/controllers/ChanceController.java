package com.example.demo.controllers;

import java.util.Date;

import com.example.demo.models.Chance;
import com.example.demo.models.Quizz;
import com.example.demo.payload.request.newChanceRequest;
import com.example.demo.repository.ChanceRepository;
import com.example.demo.repository.QuizzRepository;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chances")
public class ChanceController {
    @Autowired
    ChanceRepository chanceRepository;

    @Autowired
    QuizzRepository quizzRepository;

    @GetMapping("/getStudentChances")
    public ResponseEntity<?> getStudentChances(@RequestParam String token, @RequestParam Long quizzId)
            throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        return ResponseEntity.ok(chanceRepository.getStudentChances(username, quizzId));
    }

    @GetMapping("/getStudentPendingChances")
    public ResponseEntity<?> getStudentPendingChances(@RequestParam String token, @RequestParam Long quizzId)
            throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        return ResponseEntity.ok(chanceRepository.getStudentPendingChances(username, quizzId));
    }

    @GetMapping("/getStudentSubmittedChances")
    public ResponseEntity<?> getStudentSubmittedChances(@RequestParam String token, @RequestParam Long quizzId)
            throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        return ResponseEntity.ok(chanceRepository.getStudentSubmittedChances(username, quizzId));
    }

    @PostMapping("/addChance")
    public ResponseEntity<?> addChance(@RequestBody newChanceRequest newChance) throws FirebaseAuthException {
        System.out.println(newChance.getToken());
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(newChance.getToken());
        String username = decodedToken.getEmail();
        Quizz quizz = quizzRepository.findById(newChance.getQuizzId()).orElseThrow();
        Chance chance = new Chance();
        chance.setStatus("pending");
        chance.setUpdateDate(new Date());
        chance.setUsername(username);
        chance.setQuizz(quizz);
        chanceRepository.save(chance);
        return ResponseEntity.ok(chance.getId());
    }

    @GetMapping("/getQuizzId")
    public ResponseEntity<?> getQuizzId(@RequestParam Long chanceId){
        return ResponseEntity.ok(chanceRepository.getQuizzId(chanceId));
    }

    @GetMapping("/calculateChanceGrade")
    public ResponseEntity<?> calculateChanceGrade(@RequestParam Long chanceId){
        return ResponseEntity.ok(chanceRepository.calculateChanceGrade(chanceId));
    }

    @PostMapping("/gradeChance")
    public ResponseEntity<?> gradeChance(@RequestParam Long chanceId){
        Chance chance = chanceRepository.findById(chanceId).orElseThrow();
        chance.setGrade(chanceRepository.calculateChanceGrade(chanceId));
        chance.setGradeDate(new Date());
        chance.setStatus("submitted");
        chanceRepository.save(chance);
        return ResponseEntity.ok(chance);
    }

    
}
