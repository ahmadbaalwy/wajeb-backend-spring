package com.example.demo.controllers;

import java.util.List;

import com.example.demo.models.Classroom;
import com.example.demo.models.Course;
import com.example.demo.payload.request.newClassroomRequest;
import com.example.demo.payload.response.MessageResponse;
import com.example.demo.repository.ClassroomRepository;
import com.example.demo.repository.CourseRepository;
import com.example.demo.specs.ClassroomSpecification;
import com.example.demo.specs.SearchCriteria;
import com.example.demo.specs.SearchOperation;
import com.google.firebase.auth.FirebaseAuthException;

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
@RequestMapping("/api/classrooms")
public class ClassroomController {

    @Autowired 
    ClassroomRepository classroomRepository;

    @Autowired
    CourseRepository courseRepository;

    

    @GetMapping("/getClassrooms")
    public ResponseEntity<List<Classroom>> getClassrooms(@RequestParam Long courseId)  {
        ClassroomSpecification courseCheck = new ClassroomSpecification();
        courseCheck.add(new SearchCriteria("course", courseId, SearchOperation.EQUAL));
        List<Classroom> classrooms = classroomRepository.findAll(courseCheck);
        return ResponseEntity.ok(classrooms);

    }

    @PostMapping("/addClassroom")
    public ResponseEntity<?> addClassroom(@RequestBody newClassroomRequest newClassroom) {
        Course course = courseRepository.findById(newClassroom.getCourseId()).orElseThrow();
        Classroom classroom = new Classroom(
            newClassroom.getClassroomName(),
            newClassroom.isPrivate(),
            newClassroom.isActive(),
            newClassroom.getSchoolName(),
            newClassroom.getCategory(),
            newClassroom.getStartDate(),
            newClassroom.getEndDate()
            );
        classroom.setCourse(course);
        classroomRepository.save(classroom);
        return ResponseEntity.ok(new MessageResponse("Classroom registered successfully !"));
    }

    @DeleteMapping("/deleteClassroom")
    public ResponseEntity<?> deleteClassroom(@RequestParam Long classroomId ) {
       
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();
        classroomRepository.delete(classroom);
        return ResponseEntity.ok(new MessageResponse("Classroom Deleted Successfully!"));
    }

    @GetMapping("/editClassroom")
    public Classroom editClassroom(@RequestParam Long classroomId) {
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();
        return classroom;
    }

    @PostMapping("/editClassroom")
    public ResponseEntity<?> editClassroom(@RequestParam Long classroomId, @RequestBody newClassroomRequest newClassroom ) {
       
        Classroom classroom = classroomRepository.findById(classroomId).orElseThrow();
        classroom.setClassroomName(newClassroom.getClassroomName());
        classroom.setPrivate(newClassroom.isPrivate());
        classroom.setActive(newClassroom.isActive());
        classroom.setSchoolName(newClassroom.getSchoolName());
        classroom.setCategory(newClassroom.getCategory());
        classroom.setStartDate(newClassroom.getStartDate());
        classroom.setEndDate(newClassroom.getEndDate());
        classroomRepository.save(classroom);
        return ResponseEntity.ok(new MessageResponse("Course Edited Successfully!"));
    }

    @GetMapping("/getCourseId")
    public Long getCourseId(@RequestParam Long classroomId){
        return classroomRepository.getCourseId(classroomId);
    }

    @GetMapping("/searchForClassroom")
    public List<?> searchForClassroom(@RequestParam String courseName, @RequestParam String schoolName){
        String course_name = "%" + courseName + "%";
        String school_name = "%" + schoolName + "%";
        System.out.println(course_name);
        return classroomRepository.searchForClassroom(course_name, school_name);
    }
    
}
