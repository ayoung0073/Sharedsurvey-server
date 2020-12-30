package com.doubleslash.sharedsurvey.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequestDto {

    private Long surveyId;

    private int questionCategoryId;

    private String questionText;

    private boolean required;

    private boolean existFile;

    private String[] choiceTexts;

    private String filename;
}
