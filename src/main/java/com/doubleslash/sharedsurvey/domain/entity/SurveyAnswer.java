package com.doubleslash.sharedsurvey.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
public class SurveyAnswer {

    @Id
    private Long surveyId;

    private Long answerMemberId;

}
