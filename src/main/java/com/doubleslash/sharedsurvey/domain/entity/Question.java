package com.doubleslash.sharedsurvey.domain.entity;


import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionUpdateDto;
import lombok.AllArgsConstructor;
import lombok.Getter;

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

    private Long surveyId;

    private int questionCategoryId;

    private String questionText;

    private boolean required = false;

    private boolean existFile = false;

    private String filename = "";

    //@OneToMany(mappedBy="question", targetEntity= Answer.class)
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_survey_key")
    private List<Answer> answers;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "question_choice_key")
    private final List<QuestionChoice> questionChoices = new ArrayList<>();

    public Question(QuestionRequestDto requestDto){
        this.surveyId = requestDto.getSurveyId();
        this.questionCategoryId = requestDto.getQuestionCategoryId();
        this.questionText = requestDto.getQuestionText();
        this.required = requestDto.isRequired();
        this.existFile = requestDto.isExistFile();
    }

    public Question() {

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
