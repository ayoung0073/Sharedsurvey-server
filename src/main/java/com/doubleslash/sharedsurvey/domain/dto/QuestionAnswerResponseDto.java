package com.doubleslash.sharedsurvey.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerResponseDto {
    private String question;
    private int questionCategoryId;
    private String[] choiceTexts;
    private String answer;
}
