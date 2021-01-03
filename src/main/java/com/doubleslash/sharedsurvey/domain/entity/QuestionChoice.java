package com.doubleslash.sharedsurvey.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class QuestionChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne
    @JoinColumn(name = "surveyId")
    @JsonIgnore
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "questionId")
    @JsonIgnore
    private Question question;

    private String choiceText;

    public QuestionChoice(Survey survey, Question question, String choiceText){
        this.survey = survey;
        this.choiceText = choiceText;
        this.question = question;
    }
}
