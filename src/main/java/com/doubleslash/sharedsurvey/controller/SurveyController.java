package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.domain.dto.AnswerRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyResponseDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.entity.Answer;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.AnswerRepository;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import com.doubleslash.sharedsurvey.repository.SurveyAnswerRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import com.doubleslash.sharedsurvey.service.FileService;
import com.doubleslash.sharedsurvey.service.PointService;
import com.doubleslash.sharedsurvey.service.SurveyService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class SurveyController {

    private final SurveyService surveyService;
    private final PointService pointService;
    private final FileService fileService;

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;


    @PostMapping("/survey") // 설문조사 등록
    public Map<String, Boolean> createSurvey(@RequestPart(value = "image",required = false) MultipartFile[] files,
                                @RequestPart(value = "requestDto") SurveyRequestDto requestDto) throws IOException {
        Map<String, Boolean> map = new HashMap<>();

        map.put("success", surveyService.registerSurvey(requestDto, files));

        return map;
    }

    // 설문조사 응답하기
    // 설문조사 마감 임박 순
    // ( 종료 날짜가 같을 시 응답자 수 가 적은 설문조사 부터 정렬)
    @GetMapping("/surveys") // 모든 설문조사 나열
    public List<Survey> getSurveys(){
        return surveyRepository.findAllByOrderByEndDateAndResponseCountEndDateAfter();
    }

    @GetMapping("/surveys/end") // 종료된 설문조사
    public List<Survey> getEndSurveys() {
        return surveyRepository.findAllByOrderByEndDateAndResponseCount();
    }

    @GetMapping("/survey/{surveyId}") // 해당 설문조사의 질문들 목록
    public Map<String, Object> getSurvey(@PathVariable Long surveyId){
        return surveyService.getSurveyAndQuestions(surveyId);
    }

    @PutMapping("/survey/{surveyId}") // 설문조사 생성자가 설문조사 종료할 때
    public Map<String, Boolean> updateSurvey(@PathVariable Long surveyId, @RequestBody SurveyUpdateDto updateDto){
        Map<String, Boolean> map = new HashMap<>();
        if(surveyService.updateSurvey(surveyId,updateDto.isState())) map.put("success", true);
        else map.put("success", false);

        return map;
    }


    @GetMapping("/survey/answer/{surveyId}") // surveyId // 설문조사 응답 보기
    public Map<Object, Object> getAnswer(@PathVariable Long surveyId){

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        Survey survey = surveyRepository.findById(surveyId).get();
        if(memberRepository.findById(survey.getWriter()).get().getMemberId().equals(user.getName()))
            pointService.usePoint(user.getName(), surveyRepository.findById(surveyId).get().getPoint());

        return surveyService.getAnswers(surveyId);
    }

    @PostMapping("/survey/answer/{surveyId}") // answer 등록
    public Map<String, Object> createAnswer(@PathVariable Long surveyId, @RequestBody AnswerRequestDto requestDto) {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> optional = memberRepository.findByMemberId(user.getName());

        Map<String, Object> map = new HashMap<>();
        if (optional.isPresent()) {
            Long memberId = optional.get().getId();
            surveyService.registerAnswer(surveyId, requestDto, memberId);
            map.put("point", pointService.getPoint(surveyId, optional.get()));
            map.put("success", true);
        }
        else{
            map.put("success", false);
        }
        return map;
    }

    @GetMapping("/survey/answers")
    public List<Answer> getAnswers(){
        return answerRepository.findAll();
    }



    @GetMapping("/search/end")
    public void getSearch(@RequestParam("search") String searchVal){


    }
}
