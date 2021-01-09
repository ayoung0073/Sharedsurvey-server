package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class QuestionRepoDto {
    //Long questionId;

    String answerText;

    //int count;

    boolean gender;

    int age;
}
