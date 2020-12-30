package com.doubleslash.sharedsurvey.domain.dto;

import com.doubleslash.sharedsurvey.domain.entity.Question;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class SurveyRequestDto {

    private Long writerId;

    private String name; // 설문조사 이름

    private String description; // 설문조사 설명

    private String category;

    private Date startDate;

    private Date endDate;

    private int point;

    private boolean existFile;

    private String filename;

    List<QuestionRequestDto> questions;
}
