package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionAnswerDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnswerRequestDto {

    private Member writer;

    private List<QuestionAnswerDto> answer; // < question, answerText >
}
