package com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AgeCountDto {

    @JsonIgnore
    private int age;

    @JsonAlias("10")
    private int age10;
    @JsonAlias("20")
    private int age20;
    @JsonAlias("30")
    private int age30;
    @JsonAlias("40")
    private int age40;
    @JsonAlias("50")
    private int age50;
    @JsonAlias("60")
    private int age60;
}
