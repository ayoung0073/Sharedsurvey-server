package com.doubleslash.sharedsurvey.domain.dto.survey;

import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SurveyWidelyDto {
    // id, name, category, startDate, endDate, existFile, filename, point, state
    private Long id;

    private String name;

    private String category;

    private Date startDate;

    private Date endDate;

    private boolean existFile;

    private String filename;

    private int point;

    private boolean state;

    private int responseCount;

    public SurveyWidelyDto(Survey survey){
        this.id = survey.getId();
        this.name = survey.getName();
        this.category = survey.getCategory();
        this.startDate = survey.getStartDate();
        this.endDate = survey.getEndDate();
        this.existFile = survey.isExistFile();
        this.filename = survey.getFilename();
        this.point = survey.getPoint();
        this.state = survey.isState();
        this.responseCount = survey.getResponseCount();
    }

}
