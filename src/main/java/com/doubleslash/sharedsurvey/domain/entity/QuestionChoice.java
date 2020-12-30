package com.doubleslash.sharedsurvey.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
public class QuestionChoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long surveyId;

    private Long questionId;

    private int sub_id; // 객관식 1~5면 1~5 해당

    private String choiceText;

    public QuestionChoice(Long surveyId, Long questionId, int sub_id, String choiceText){
        this.surveyId = surveyId;
        this.choiceText = choiceText;
        this.sub_id = sub_id;
        this.questionId = questionId;
    }
}
