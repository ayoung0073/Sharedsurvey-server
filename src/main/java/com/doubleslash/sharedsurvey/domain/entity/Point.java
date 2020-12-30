package com.doubleslash.sharedsurvey.domain.entity;

import com.doubleslash.sharedsurvey.domain.Timestamped;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Point extends Timestamped {
    // id, question(varchar), questionCategoryId

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private boolean type; // true이면 포인트 get, false이면 포인트 use

    private Long surveyId;

    private int pointVal;

    @Transient
    private String surveyName;


    public Point(Long memberId, int pointVal, boolean type, Long surveyId){
        this.memberId = memberId;
        this.type = type;
        this.surveyId = surveyId;
        this.pointVal = pointVal;
    }

    public Point(){}

    public void setSurveyName(String surveyName) {
        this.surveyName = surveyName;
    }
}
