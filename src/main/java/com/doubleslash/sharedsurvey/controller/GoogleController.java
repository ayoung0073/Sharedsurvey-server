package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.domain.dto.google.GoogleRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.response.SuccessDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.utils.GoogleSurveyParsing;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class GoogleController {

    private final GoogleSurveyParsing googleSurveyParsing;

    @PostMapping("/survey/google") // 설문조사 등록
    public SuccessDto createGoogleSurvey(@RequestPart(value = "image", required = false) MultipartFile[] files,
                                             @RequestPart(value = "requestDto") GoogleRequestDto requestDto,
                                             @AuthenticationPrincipal Member member) throws IOException {

        Map<String, Boolean> map = new HashMap<>();
        if (member != null)
            return new SuccessDto(googleSurveyParsing.registerSurvey(requestDto.getUrl(), requestDto, files, member));// map.put("success", googleSurveyParsing.parsing(url, requestDto));
        else {
            return new SuccessDto(false);
        }
    }
}
