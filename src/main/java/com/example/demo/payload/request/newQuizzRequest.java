package com.example.demo.payload.request;

import java.util.Date;

public class newQuizzRequest {

    private String quizzName;
    private boolean active;
    private int maxChances;
    private int grade;
    private Date creationDate;
    private Long classroomId;

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

    public Long getClassroomId() {
        return classroomId;
    }

    public void setClassroomId(Long classroomId) {
        this.classroomId = classroomId;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }
    
}
