package com.doubleslash.sharedsurvey.domain.entity;

import com.doubleslash.sharedsurvey.domain.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@Entity
public class Point extends Timestamped {
    // id, question(varchar), questionCategoryId

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private boolean type;

    private Long surveyId;

    public Point(Long memberId, boolean type, Long surveyId){
        this.memberId = memberId;
        this.type = type;
        this.surveyId = surveyId;
    }

    public Point(){}

}
