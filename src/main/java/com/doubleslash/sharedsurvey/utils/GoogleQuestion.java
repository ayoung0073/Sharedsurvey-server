package com.doubleslash.sharedsurvey.utils;

import com.doubleslash.sharedsurvey.domain.entity.Question;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Arrays;

@Setter
@Getter
public class GoogleQuestion {


    String questionText;

    int category;

    boolean required;

    String[] choiceTexts;

    // 나중에 set 할 거

    Survey survey;

    private Question question;

    private String choiceText;



    @Override
    public String toString() {
        return "GoogleQuestion{" +
                "questionText='" + questionText + '\'' +
                ", category='" + category + '\'' +
                ", required=" + required +
                ", questionChoices=" + Arrays.toString(choiceTexts) +
                '}';
    }
}
