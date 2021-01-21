package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import com.doubleslash.sharedsurvey.domain.entity.Question;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionAnswerDto {

    private Long questionId;

    private Object answerText;
}
