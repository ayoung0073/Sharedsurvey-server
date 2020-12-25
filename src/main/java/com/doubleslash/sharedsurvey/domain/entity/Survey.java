package com.doubleslash.sharedsurvey.domain.entity;

import com.doubleslash.sharedsurvey.domain.Timestamped;
import com.doubleslash.sharedsurvey.domain.dto.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyUpdateDto;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;
import java.util.Date;

@Getter
@Entity
public class Survey extends Timestamped {
    // id, writer, name, description, startDate, endDate,
    // regDate, state, responseCount, point, picture

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long writer; // Member - id

    private String name;

    private String description;

    private Date startDate;

    private Date endDate;

    private boolean state;

    private int point;

    private String picture = "";

    private int responseCount;

    public Survey(SurveyRequestDto requestDto){
        this.writer = requestDto.getWriterId();
        this.name = requestDto.getName();
        this.description = requestDto.getDescription();
        this.startDate = requestDto.getStartDate();
        this.endDate = requestDto.getEndDate();
        this.state = true;
        this.point = requestDto.getPoint();
        this.picture = requestDto.getPicture();
    }

    public Survey() {

    }

    public void updateSurvey(boolean state){
        this.state = state;
    }

    public boolean updateCount(){
        this.responseCount ++;
        return true;
    }
}
