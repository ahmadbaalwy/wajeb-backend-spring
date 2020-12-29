package com.example.demo.models;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "courses")
public class Course {
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
	@Size(max = 50)
    private String courseName;
    
    @NotNull
    private String user;

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public Course() {
    }

    public Course(@NotBlank @Size(max = 50) String courseName, @NotNull String user) {
        
        this.courseName = courseName;
        this.user = user;
    }

}
