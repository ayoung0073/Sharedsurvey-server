package com.doubleslash.sharedsurvey.domain.entity;

import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Entity
public class SurveyWriter {

    @Id
    private Long surveyId;

    private Long writerId;
}
