package com.doubleslash.sharedsurvey.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Answer {
    // answer - id, (surveyId, questionId), writer, answer
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne // 하나의 게시글에 여러개 답변 -> ManyToOne
    @JoinColumn(name="questionId")
    @JsonIgnore
    private Question question;

    @ManyToOne // 하나의 게시글에 여러개 답변 -> ManyToOne
    @JoinColumn(name="writerId")
    @JsonIgnore
    private Member writer;

    private String answerText;

    private Long surveyId;

    public Answer(){};

    public Answer(Member writer, Question question, String answerText, Long surveyId) {
        this.question = question;
        this.writer = writer;
        this.answerText = answerText;
        this.surveyId = surveyId;
    }
}
