package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.specs.CourseSpecification;
import com.example.demo.specs.SearchCriteria;
import com.example.demo.specs.SearchOperation;

import org.apache.tomcat.jni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import com.example.demo.models.Course;
import com.example.demo.models.User;
import com.example.demo.payload.request.newCourseRequest;
import com.example.demo.payload.response.MessageResponse;;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/myCourses")
    public List<Course> Courses(@RequestParam Long userId) {
        CourseSpecification ownerCheck = new CourseSpecification();
        ownerCheck.add(new SearchCriteria("user", userId, SearchOperation.EQUAL));
        List<Course> courses = courseRepository.findAll(ownerCheck);
        return courses;

    }

    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody newCourseRequest newCourse) {
        courseRepository.save(new Course(newCourse.getCourseName(), userRepository.findById(newCourse.getUserId()).orElseThrow()));
        return ResponseEntity.ok(new MessageResponse("Course registered successfully !"));
    }
    
}
