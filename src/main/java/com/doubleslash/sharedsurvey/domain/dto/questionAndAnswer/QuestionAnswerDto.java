package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerDto {

    private Long questionId;

    private String answerText;
}
