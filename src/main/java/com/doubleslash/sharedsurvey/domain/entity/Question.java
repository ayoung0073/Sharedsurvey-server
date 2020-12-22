package com.doubleslash.sharedsurvey.domain.entity;


import com.doubleslash.sharedsurvey.domain.dto.QuestionRequestDto;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Question {
    // id, question(varchar), questionCategoryId

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long surveyId;

    private int questionCategoryId;

    private String questionText;

    public Question(QuestionRequestDto requestDto){
        this.surveyId = requestDto.getSurveyId();
        this.questionCategoryId = requestDto.getQuestionCategoryId();
        this.questionText = requestDto.getQuestionText();
    }

    public Question() {

    }
}
