package com.doubleslash.sharedsurvey.domain.dto;

import com.doubleslash.sharedsurvey.domain.entity.Survey;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.Date;

@Getter
@Setter
public class SurveyResponseDto {
    private Long id;

    private Long writer; // Member - id

    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private boolean state;

    private int point;

    private String picture;

    private int responseCount;

    public SurveyResponseDto(Survey survey){
        this.id = survey.getId();
        this.description = survey.getDescription();
        this.endDate = survey.getEndDate();
        this.startDate = survey.getStartDate();
        this.writer = survey.getWriter();
        this.name = survey.getName();
        this.picture = survey.getFilename();
        this.state = survey.isState();
        this.point = survey.getPoint();
    }

}
