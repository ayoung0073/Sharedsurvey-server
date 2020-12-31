package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.domain.dto.AnswerRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SuccessDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.entity.Answer;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Point;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.*;
import com.doubleslash.sharedsurvey.service.AnswerService;
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
    private final AnswerService answerService;

    private final MemberRepository memberRepository;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;
    private final PointRepository pointRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;


    @PostMapping("/survey") // 설문조사 등록
    public Map<String, Boolean> createSurvey(@RequestPart(value = "image", required = false) MultipartFile[] files,
                                             @RequestPart(value = "requestDto") SurveyRequestDto requestDto,
                                             @AuthenticationPrincipal Member member) throws IOException {
        Map<String, Boolean> map = new HashMap<>();
        if (member != null)
            map.put("success", surveyService.registerSurvey(requestDto, files, member));
        else {
            map.put("success", false);
        }
        return map;
    }

    // 설문조사 응답하기
    // 설문조사 마감 임박 순
    // ( 종료 날짜가 같을 시 응답자 수 가 적은 설문조사 부터 정렬)
    @GetMapping("/surveys") // 모든 설문조사 나열
    public List<Survey> getSurveys() {
        return surveyRepository.findAllByOrderByEndDateAndResponseCountEndDateAfter();
    }

    @GetMapping("/surveys/end") // 종료된 설문조사
    public List<Survey> getEndSurveys() {
        return surveyRepository.findAllByOrderByEndDateAndResponseCount();
    }

    @GetMapping("/survey/{surveyId}") // 해당 설문조사의 질문들 목록
    public Map<String, Object> getSurvey(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        return surveyService.getSurveyAndQuestions(surveyId, member.getId());
    }

    @PutMapping("/survey/{surveyId}") // 설문조사 생성자가 설문조사 종료할 때
    public Map<String, Boolean> updateSurvey(@PathVariable Long surveyId, @RequestBody SurveyUpdateDto updateDto, @AuthenticationPrincipal Member member) {
        Map<String, Boolean> map = new HashMap<>();
        if (member != null)
            if (surveyService.updateSurvey(surveyId, updateDto)) map.put("success", true);
            else map.put("success", false);
        else map.put("success", false);

        return map;
    }

    @DeleteMapping("/survey/{surveyId}")
    public SuccessDto deleteSurvey(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        if (member != null) {
            surveyRepository.deleteById(surveyId);
            return new SuccessDto(true);
        }
        return new SuccessDto(false);
    }

    @PostMapping("/survey/{surveyId}") // answer 등록
    public Map<String, Object> createAnswer(@PathVariable Long surveyId, @RequestBody AnswerRequestDto requestDto, @AuthenticationPrincipal Member member){
        //Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> map = new HashMap<>();
        if (member != null) {
            if(surveyAnswerRepository.findBySurveyIdAndAnswerMemberId(surveyId, member.getId()).isPresent()) {
                map.put("success", false);
                map.put("message", "이미 답변한 설문조사입니다.");
            }
            else {
                //Optional<Member> optional = memberRepository.findByMemberId(member.getMemberId());
                //if (optional.isPresent()) {
                Long memberId = member.getId();
                int point = answerService.registerAnswer(surveyId, requestDto, memberId);
                map.put("point", pointService.getPoint(surveyId, point, member));
                map.put("success", true);
            }
            //}
        } else {
            map.put("success", false);
        }
        return map;
    }

    @GetMapping("/survey/{surveyId}/answer") // surveyId // 설문조사 응답 보기
    public Map<Object, Object> getAnswer(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        // Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Map<Object, Object> map = new HashMap<>();
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));

        if (member != null) {
            map.put("success", true);
            if (memberRepository.findById(survey.getWriter()).orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다.")).getMemberId().equals(member.getMemberId())) {
                pointService.usePoint(member, surveyRepository.findById(surveyId).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다.")).getPoint());
            }
            map.put("answers", answerService.getAnswers(surveyId));
        }
        else {
            map.put("success", false);
            map.put("message", "유효하지 않은 토큰");
        }
        return map;
    }

    @GetMapping("/survey/answers")
    public List<Answer> getAnswers() {
        return answerRepository.findAll();
    }

    @GetMapping("/survey")
    public List<Survey> getSearch(@RequestParam("search") String searchVal) {
        return surveyRepository.findAllByEndDateAfterAndNameContainingOrderByEndDate(new Date(), searchVal);
    }

    @GetMapping("/survey/end") // 종료된 설문조사 search
    public List<Survey> getSearchEnd(@RequestParam("search") String searchVal) {
        return surveyRepository.findAllByEndDateBeforeAndNameContainingOrderByEndDate(new Date(), searchVal);
    }

    @GetMapping("/survey/{surveyId}/me") // surveyId // 설문조사 응답 보기
    public Map<String, Object> getAnswerByMember(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        Map<String, Object> map = new HashMap<>();
        if (member != null) {
            map.put("success",true);
            map.put("response",answerService.getQuestionAndAnswerBymemberId(surveyId, member.getId()));
        }
        else map.put("success", false);

        return map;
    }

    @GetMapping("/{memberId}")
    public Map<String, Object> myPage(@PathVariable Long memberId, @AuthenticationPrincipal Member member){
        Map<String, Object> map = new HashMap<>();
        if(memberId.equals(member.getId())){
            map.put("success", true);
            // 내가 올렸던 종료된 설문조사
            map.put("end", surveyRepository.findAllByEndDateBeforeAndWriterOrderByEndDate(new Date(), memberId));

            // 내가 올렸던 진행 중인 설문조사
            map.put("progress", surveyRepository.findAllByEndDateAfterAndWriterOrderByEndDate(new Date(), memberId));

            // 포인트 내역

            List<Point> pointList = pointRepository.findAllByMemberId(memberId);
            for(Point p : pointList){
                p.setSurveyName(surveyRepository.findById(p.getSurveyId())
                        .orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다.")).getName());
            }
            map.put("point", pointRepository.findAllByMemberId(memberId));
        }
        else map.put("success", false);

        return map;
    }

}

