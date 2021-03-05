package com.example.demo.models;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.persistence.Lob;
import javax.persistence.Basic;
import javax.persistence.Column;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.engine.FetchStyle;

@Entity
@Table(name="questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;

    private int sequence;

    private int score;

    private String text;
    
    //@JsonInclude()
    //@Transient
    @Column(name = "picByte", length = 1000000)
    private byte[] picByte;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quizz_id")
    @JsonBackReference
    @NotBlank
    private Quizz quizz;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "question")
    @JsonManagedReference
    private Set<QuizzAnswer> quizzAnswers;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Quizz getQuizz() {
        return quizz;
    }

    public void setQuizz(Quizz quizz) {
        this.quizz = quizz;
    }

    public Question() {
    }

    public Question(String type, int sequence, int score, String text, byte[] picByte) {
        this.type = type;
        this.sequence = sequence;
        this.score = score;
        this.text = text;
        this.picByte = picByte;
      
        
    }

	public Question(String type, int sequence, int score, String text) {
        this.type = type;
        this.sequence = sequence;
        this.score = score;
        this.text = text;      
        
    }

	public Set<QuizzAnswer> getQuizzAnswers() {
        return quizzAnswers;
    }

    public void setQuizzAnswers(Set<QuizzAnswer> quizzAnswers) {
        this.quizzAnswers = quizzAnswers;
    }

    public byte[] getPicByte() {
        return picByte;
    }

    public void setPicByte(byte[] picByte) {
        this.picByte = picByte;
    }

    
}
