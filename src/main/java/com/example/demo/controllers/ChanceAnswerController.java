package com.example.demo.controllers;

import com.example.demo.models.ChanceAnswer;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ChanceAnswerRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/chanceAnswers")
public class ChanceAnswerController {
    @Autowired
    ChanceAnswerRepository chanceAnswerRepository;

    @PostMapping("/newChanceAnswers")
    public ResponseEntity<?> newChanceAnswers(@RequestParam Long chanceId){
        chanceAnswerRepository.newChanceAnswers(chanceId);
        return ResponseEntity.ok(new MessageResponse("the new chanceAnswers were initialized successfully!"));
    }

    @GetMapping("/getChanceAnswerData")
    public ResponseEntity<?> getChanceAnswerData(@RequestParam Long chanceId, @RequestParam Long questionId) {
        
        return ResponseEntity.ok(chanceAnswerRepository.getChanceAnswerData(chanceId, questionId));
    }

    @PostMapping("/editChanceAnswer")
    public ResponseEntity<?> editChanceAnswer(@RequestParam Long chanceAnswerId, @RequestParam boolean selected){
        ChanceAnswer chanceAnswer = chanceAnswerRepository.findById(chanceAnswerId).orElseThrow();
        chanceAnswer.setSelected(selected);
        chanceAnswerRepository.save(chanceAnswer);
        return ResponseEntity.ok(new MessageResponse("chanceAnswer edited successfully!"));
    }


}
