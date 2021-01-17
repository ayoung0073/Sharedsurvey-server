package com.doubleslash.sharedsurvey.domain.dto.survey;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionUpdateDto;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SurveyUpdateDto {

    private Long questionId;

    private String name; // 설문조사 이름

    private String description; // 설문조사 설명

    private LocalDate startDate;

    private LocalDate endDate;

    private int point;

    private boolean state;

    List<QuestionUpdateDto> questions;

}
