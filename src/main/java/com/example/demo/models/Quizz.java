package com.example.demo.models;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name="quizzes")
public class Quizz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String quizzName;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean active;

    private int maxChances;
    
    private int grade;

    private Date creationDate;

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean allowReview;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "classroom_id")
    @JsonBackReference
    @NotBlank
    private Classroom classroom;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "quizz")
    @JsonManagedReference
    private Set<Question> questions;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "quizz")
    @JsonManagedReference
    private Set<Chance> chances;

    public Quizz(){}

    public Quizz(String quizzName, boolean active, int maxChances, int grade, boolean allowReview) {
        this.quizzName = quizzName;
        this.active = active;
        this.maxChances = maxChances;
        this.grade = grade;
        this.allowReview = allowReview;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuizzName() {
        return quizzName;
    }

    public void setQuizzName(String quizzName) {
        this.quizzName = quizzName;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public int getMaxChances() {
        return maxChances;
    }

    public void setMaxChances(int maxChances) {
        this.maxChances = maxChances;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    

    public boolean isAllowReview() {
        return allowReview;
    }

    public void setAllowReview(boolean allowReview) {
        this.allowReview = allowReview;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Set<Chance> getChances() {
        return chances;
    }

    public void setChances(Set<Chance> chances) {
        this.chances = chances;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

   
    

    
}
