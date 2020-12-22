package com.doubleslash.sharedsurvey.domain.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class AnswerRequestDto {

    private Long writerId;

    private List<QuestionAnswerDto> answer; // < questionId, answerText >
}
