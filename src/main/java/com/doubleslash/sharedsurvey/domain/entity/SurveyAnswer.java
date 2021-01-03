package com.doubleslash.sharedsurvey.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@Getter
@Entity
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "surveyId")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "answerMemberId")
    @JsonIgnore
    private Member answerMember;

    public SurveyAnswer(Survey survey, Member answerMember){
        this.survey = survey;
        this.answerMember = answerMember;
    }

    public SurveyAnswer(){}
}
