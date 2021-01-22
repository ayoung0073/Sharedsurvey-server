package com.doubleslash.sharedsurvey.domain.dto.survey;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class SurveyRequestDto {

    private Member writer;

    private String name; // 설문조사 이름

    private String description; // 설문조사 설명

    private String category;

    private LocalDate startDate;

    private LocalDate endDate;

    private int point;

    private boolean existFile;

    private String filename;

    QuestionRequestDto[] questions;
}
