package com.doubleslash.sharedsurvey.domain.entity;

import com.doubleslash.sharedsurvey.domain.Timestamped;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Point extends Timestamped {
    // id, question(varchar), questionCategoryId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "memberId")
    @JsonIgnore
    private Member member;

    private boolean type; // true이면 포인트 get, false이면 포인트 use

    @ManyToOne
    @JoinColumn(name = "surveyId")
    private Survey survey;


    public Point(Member member, boolean type, Survey survey){
        this.member = member;
        this.type = type;
        this.survey = survey;
    }

    public Point(){}
}
