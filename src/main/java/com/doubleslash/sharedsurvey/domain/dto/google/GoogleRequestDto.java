package com.doubleslash.sharedsurvey.domain.dto.google;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class GoogleRequestDto {
    private String url;

    private Member writer;

    private String category;

    private Date startDate;

    private Date endDate;

    private int point;

    private boolean existFile;

    private String filename;

}
