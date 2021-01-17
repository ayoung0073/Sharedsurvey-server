package com.doubleslash.sharedsurvey.domain.dto.google;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class GoogleRequestDto {
    private String url;

    private Member writer;

    private String category;

    private LocalDate startDate;

    private LocalDate endDate;

    private int point;

    private boolean existFile;

    private String filename;

}
