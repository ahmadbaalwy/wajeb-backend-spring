package com.example.demo.controllers;

import java.util.Date;
import java.util.List;

import com.example.demo.models.Classroom;
import com.example.demo.models.Enrollment;
import com.example.demo.payload.request.newEnrollmentRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.EnrollmentRepository;
import com.example.demo.repository.EnrollmentRepository.enrollmentsDetails;
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
@RequestMapping("/api/enrollments")
public class EnrollmentController {
    @Autowired
    EnrollmentRepository enrollmentRepository;

    @Autowired
    ClassroomRepository classroomRepository;

    @GetMapping("/getEnrollments")
    public ResponseEntity<?> getEnrollments(@RequestParam String token) throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();        
        List<enrollmentsDetails> enrollments = enrollmentRepository.getMyEnrollments(username);
        return ResponseEntity.ok(enrollments);
    }

    @PostMapping("/addEnrollment")
    public ResponseEntity<?> addEnrollment(@RequestBody newEnrollmentRequest newEnrollment) {
        Classroom classroom = classroomRepository.findById(newEnrollment.getClassroom_id()).orElseThrow();
        Enrollment enrollment = new Enrollment();
        enrollment.setStatus(newEnrollment.getStatus());
        enrollment.setRequestDate(new Date());
        enrollment.setUsername(newEnrollment.getUsername());
        enrollment.setClassroom(classroom);
        enrollmentRepository.save(enrollment);
        return ResponseEntity.ok(new MessageResponse("Enrollment registered successfully !"));
    }

    @GetMapping("/getPendingEnrollments")
    public ResponseEntity<?> getPendingEnrollments(@RequestParam Long classroomId){
        return ResponseEntity.ok(enrollmentRepository.getPendingEnrollments(classroomId));
    }

    @GetMapping("/getApprovedEnrollments")
    public ResponseEntity<?> getApprovedEnrollments(@RequestParam Long classroomId){
        return ResponseEntity.ok(enrollmentRepository.getApprovedEnrollments(classroomId));
    }

    @PostMapping("/approvePendingEnrollment")
    public ResponseEntity<?> approvePendingEnrollment(@RequestParam Long enrollmentId){
        Enrollment enrollment = enrollmentRepository.findById(enrollmentId).orElseThrow();
        enrollment.setStatus("approved");
        enrollment.setApprovalDate(new Date());
        enrollmentRepository.save(enrollment);
        return ResponseEntity.ok(new MessageResponse("Enrollment approved successfully"));
    }
}
