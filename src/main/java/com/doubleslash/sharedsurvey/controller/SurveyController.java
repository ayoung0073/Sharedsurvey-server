package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.domain.dto.AnswerRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SuccessDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.entity.Answer;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.AnswerRepository;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import com.doubleslash.sharedsurvey.repository.SurveyAnswerRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import com.doubleslash.sharedsurvey.service.PointService;
import com.doubleslash.sharedsurvey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@RestController
public class SurveyController {

    private final SurveyService surveyService;
    private final PointService pointService;

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;


    @PostMapping("/survey") // 설문조사 등록
    public Map<String, Boolean> createSurvey(@RequestPart(value = "image",required = false) MultipartFile[] files,
                                @RequestPart(value = "requestDto") SurveyRequestDto requestDto,
                                             @AuthenticationPrincipal Member member) throws IOException {
        Map<String, Boolean> map = new HashMap<>();
        if(member != null)
            map.put("success", surveyService.registerSurvey(requestDto, files, member));
        else
            map.put("success", false);
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
    public Map<String, Boolean> updateSurvey(@PathVariable Long surveyId, @RequestBody SurveyUpdateDto updateDto, @AuthenticationPrincipal Member member){
        Map<String, Boolean> map = new HashMap<>();
        if(member != null)
            if(surveyService.updateSurvey(surveyId,updateDto.isState(), member.getMemberId())) map.put("success", true);
            else map.put("success", false);
        else map.put("success", false);

        return map;
    }

    @DeleteMapping("/survey/{surveyId}")
    public SuccessDto deleteSurvey(@PathVariable Long surveyId, @AuthenticationPrincipal Member member){
        if(member != null) {
            surveyRepository.deleteById(surveyId);
            return new SuccessDto(true);
        }
        return new SuccessDto(false);
    }

    @GetMapping("/survey/answer/{surveyId}") // surveyId // 설문조사 응답 보기
    public Map<Object, Object> getAnswer(@PathVariable Long surveyId, @AuthenticationPrincipal Member member){
        // Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Map<Object, Object> map = new HashMap<>();
        Survey survey = surveyRepository.findById(surveyId).get();
        if(member != null)
            if(memberRepository.findById(survey.getWriter()).get().getMemberId().equals(member.getMemberId())) {
                pointService.usePoint(member.getMemberId(), surveyRepository.findById(surveyId).get().getPoint());
                return surveyService.getAnswers(surveyId);
            }
            else{
                return surveyService.getAnswers(surveyId);
            }
        else {
            map.put("success", false);
            map.put("response", "유효하지 않은 토큰");
        }
        return map;
    }

    @PostMapping("/survey/answer/{surveyId}") // answer 등록
    public Map<String, Object> createAnswer(@PathVariable Long surveyId, @RequestBody AnswerRequestDto requestDto, @AuthenticationPrincipal Member member) {
        //Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> map = new HashMap<>();
        if(member != null) {
            //Optional<Member> optional = memberRepository.findByMemberId(member.getMemberId());
            //if (optional.isPresent()) {
            Long memberId = member.getId();
            surveyService.registerAnswer(surveyId, requestDto, memberId);
            map.put("point", pointService.getPoint(surveyId, member));
            map.put("success", true);
            //}
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

    @GetMapping("/survey")
    public List<Survey> getSearch(@RequestParam("search") String searchVal){
        return surveyRepository.findAllByEndDateAfterAndNameContainingOrderByEndDate(new Date(), searchVal);
    }

    @GetMapping("/survey/end") // 종료된 설문조사 search
    public List<Survey> getSearchEnd(@RequestParam("search") String searchVal){
        return surveyRepository.findAllByEndDateBeforeAndNameContainingOrderByEndDate(new Date(), searchVal);
    }
}
