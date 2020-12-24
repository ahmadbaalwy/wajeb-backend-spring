package com.example.demo.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import com.example.demo.repository.CourseRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.security.jwt.JwtUtils;
import com.example.demo.security.services.UserDetailsServiceImpl;
import com.example.demo.specs.CourseSpecification;
import com.example.demo.specs.SearchCriteria;
import com.example.demo.specs.SearchOperation;

import org.apache.tomcat.jni.Status;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
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
    JwtUtils jwtUtils;

    User user;

    @Autowired
    CourseRepository courseRepository;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/myCourses")
    public List<Course> Courses(@RequestParam String token) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        user = userRepository.findByUsername(username).orElseThrow();
        CourseSpecification ownerCheck = new CourseSpecification();
        ownerCheck.add(new SearchCriteria("user", user, SearchOperation.EQUAL));
        List<Course> courses = courseRepository.findAll(ownerCheck);
        return courses;

    }

    @PostMapping("/addCourse")
    public ResponseEntity<?> addCourse(@RequestBody newCourseRequest newCourse) {
        String username = jwtUtils.getUserNameFromJwtToken(newCourse.getToken());
        user = userRepository.findByUsername(username).orElseThrow();
        courseRepository.save(new Course(newCourse.getCourseName(), user)) ;
        return ResponseEntity.ok(new MessageResponse("Course registered successfully !"));
    }

    @PostMapping("/deleteCourse")
    public ResponseEntity<?> deleteCourse(@RequestParam String token, @RequestParam Long courseId ){
        String username = jwtUtils.getUserNameFromJwtToken(token);
        user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        if (course.getUser().getId()==user.getId()){
            courseRepository.delete(course);
            return ResponseEntity.ok(new MessageResponse("Course Deleted Successfully!"));
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);

    }

    @GetMapping("/editCourse")
    public Course editCourse(@RequestParam String token, @RequestParam Long courseId) {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        return course;

    }

    @PostMapping("/editCourse")
    public ResponseEntity<?> editCourse(@RequestParam String token, @RequestParam Long courseId, @RequestParam String courseName ){
        String username = jwtUtils.getUserNameFromJwtToken(token);
        user = userRepository.findByUsername(username).orElseThrow();
        Course course = courseRepository.findById(courseId).orElseThrow();
        course.setCourseName(courseName);
        if (course.getUser().getId()==user.getId()){
            courseRepository.save(course);
            return ResponseEntity.ok(new MessageResponse("Course Deleted Successfully!"));
        }
        return ResponseEntity.ok(HttpStatus.UNAUTHORIZED);

    }
    
}
