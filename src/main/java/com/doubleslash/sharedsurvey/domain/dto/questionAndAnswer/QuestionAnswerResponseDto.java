package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
