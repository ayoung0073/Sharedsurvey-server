package com.doubleslash.sharedsurvey.domain.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Answer {
    // answer - id, (surveyId, questionId), writer, answer
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long questionId;

    private Long writerId;

    private String answerText;

    public Answer(){};

    public Answer(Long writerId, Long questionId, String answerText) {
        this.questionId = questionId;
        this.writerId = writerId;
        this.answerText = answerText;
    }
}
