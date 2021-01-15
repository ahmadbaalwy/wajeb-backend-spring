package com.example.demo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonBackReference;



@Entity
@Table(name="chance_answers")
public class ChanceAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chance_id")
    @JsonBackReference
    @NotBlank
    private Chance chance;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quizzAnswer_id")
    @JsonBackReference
    @NotBlank
    private QuizzAnswer quizzAnswer;

    @Column(nullable = false, columnDefinition = "boolean default false" )
    private boolean selected = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Chance getChance() {
        return chance;
    }

    public void setChance(Chance chance) {
        this.chance = chance;
    }

    public QuizzAnswer getQuizzAnswer() {
        return quizzAnswer;
    }

    public void setQuizzAnswer(QuizzAnswer quizzAnswer) {
        this.quizzAnswer = quizzAnswer;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public ChanceAnswer() {
    }
}
