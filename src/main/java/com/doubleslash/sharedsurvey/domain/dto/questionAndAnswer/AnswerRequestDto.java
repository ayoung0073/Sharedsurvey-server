package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionAnswerDto;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerRequestDto {

    private Long writerId;

    private List<QuestionAnswerDto> answer; // < questionId, answerText >
}
