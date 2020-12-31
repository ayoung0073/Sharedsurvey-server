package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

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
