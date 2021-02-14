package com.example.demo.payload.request;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public class newQuestionRequest {

    private String type;
    private String sequence;
    private String score;
    private String text;
    private MultipartFile picByte;
    private String quizzId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

  

    public String getQuizzId() {
        return quizzId;
    }

    public void setQuizzId(String quizzId) {
        this.quizzId = quizzId;
    }

    public MultipartFile getPicByte() {
        return picByte;
    }

    public void setPicByte(MultipartFile picByte) {
        this.picByte = picByte;
    }
    
}
