package com.doubleslash.sharedsurvey.domain.dto.point;

import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
public class PointInfoDto {

    private Long id;

    private boolean type; // true이면 포인트 get, false이면 포인트 use

    private Long surveyId;

    private String surveyName;

    private int point;

    private LocalDateTime createdAt;
}
