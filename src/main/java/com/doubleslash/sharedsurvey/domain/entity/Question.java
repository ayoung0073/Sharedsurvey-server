package com.doubleslash.sharedsurvey.domain.entity;


import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionUpdateDto;
import com.doubleslash.sharedsurvey.utils.GoogleQuestion;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@Getter
@Entity
public class Question {
    // id, question(varchar), questionCategoryId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "surveyId")
    private Survey survey;

    private int questionCategoryId;

    private String questionText;

    private boolean required = false;

    private boolean existFile = false;

    private String filename = "";

    //@OneToMany(mappedBy="question", targetEntity= Answer.class)
    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"question"})
    @JsonIgnore
    private List<Answer> answers;

    @OneToMany(mappedBy = "question", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    @JsonIgnoreProperties({"question, survey, id"})
    private List<QuestionChoice> questionChoices;


    public Question(QuestionRequestDto requestDto){
        this.survey = requestDto.getSurvey();
        this.questionCategoryId = requestDto.getQuestionCategoryId();
        this.questionText = requestDto.getQuestionText();
        this.required = requestDto.isRequired();
        this.existFile = requestDto.isExistFile();
    }

    public Question() {

    }

    public Question(GoogleQuestion question){
        this.survey =  question.getSurvey();
        this.questionCategoryId = question.getCategory();
        this.questionText = question.getQuestionText();
        this.required = question.isRequired();
        this.existFile = false;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void updateQuestion(QuestionUpdateDto updateDto){
        this.questionCategoryId = updateDto.getQuestionCategoryId();
        this.questionText = updateDto.getQuestionText();
        this.required = updateDto.isRequired();
    }

}
