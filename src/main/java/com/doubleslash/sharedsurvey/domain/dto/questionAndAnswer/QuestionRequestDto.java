package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import com.doubleslash.sharedsurvey.domain.entity.Survey;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuestionRequestDto {

    private Survey survey;

    private int questionCategoryId;

    private String questionText;

    private boolean required;

    private boolean existFile;

    private String[] choiceTexts;

    private String filename;
}
