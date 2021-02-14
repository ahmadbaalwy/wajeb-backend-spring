package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.services.FirebaseAppService;
import com.example.demo.specs.CourseSpecification;
import com.example.demo.specs.SearchCriteria;
import com.example.demo.specs.SearchOperation;

import org.apache.tomcat.jni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.demo.models.Course;
import com.example.demo.models.User;
import com.example.demo.payload.request.newCourseRequest;
import com.example.demo.payload.response.JwtResponse;
import com.example.demo.payload.response.MessageResponse;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.database.FirebaseDatabase;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    User user;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/tokenVerify")
    public String toeknVerify(@RequestParam String token) throws FirebaseAuthException, IOException {

        /*InputStream serviceAccount = this.getClass().getResourceAsStream("/wajeb-project-firebase-adminsdk-sgo80-e335aab765.json");    
        FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();
        FirebaseApp defaultApp = FirebaseApp.initializeApp(options);\*/
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        return decodedToken.getEmail();
        
    }
    


    @GetMapping("/myCourses")
    public ResponseEntity<?> Courses(@RequestParam String token) throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        //user = userRepository.findByUsername(username).orElseThrow();
        CourseSpecification ownerCheck = new CourseSpecification();
        ownerCheck.add(new SearchCriteria("user", username, SearchOperation.EQUAL));
        List<Course> courses = courseRepository.findAll(ownerCheck);
        //return ResponseEntity.ok(courses);
        //return ResponseEntity.ok(courses);
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody newCourseRequest newCourse) throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(newCourse.getToken());
        String username = decodedToken.getEmail();
        //user = userRepository.findByUsername(username).orElseThrow();
        courseRepository.save(new Course(newCourse.getCourseName(), username)) ;
        return ResponseEntity.ok(new MessageResponse("Course registered successfully !"));
    }

    @PostMapping("/deleteCourse")
    public ResponseEntity<?> deleteCourse(@RequestParam String token, @RequestParam Long courseId )
            throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        //user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if (course.getUser().contentEquals(username)){
            courseRepository.delete(course);
            return ResponseEntity.ok(new MessageResponse("Course Deleted Successfully!"));
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/editCourse")
    public Course editCourse(@RequestParam String token, @RequestParam Long courseId) throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        //user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        return course;

    }

    @PostMapping("/editCourse")
    public ResponseEntity<?> editCourse(@RequestParam String token, @RequestParam Long courseId, @RequestParam String courseName )
            throws FirebaseAuthException {
        FirebaseAuth defaultAuth = FirebaseAuth.getInstance();
        FirebaseToken decodedToken = defaultAuth.verifyIdToken(token);
        String username = decodedToken.getEmail();
        //user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.setCourseName(courseName);
        if (course.getUser().contentEquals(username)){
            courseRepository.save(course);
            return ResponseEntity.ok(new MessageResponse("Course Deleted Successfully!"));
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);

    }
    
}
