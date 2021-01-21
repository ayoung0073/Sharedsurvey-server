package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.AnswerRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRepoDto;
import com.doubleslash.sharedsurvey.domain.dto.response.SuccessDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyWidelyDto;
import com.doubleslash.sharedsurvey.domain.entity.Answer;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Question;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.service.QuestionAnswerService;
import com.doubleslash.sharedsurvey.service.PointService;
import com.doubleslash.sharedsurvey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@RequiredArgsConstructor
@RestController
public class SurveyController {

    private final SurveyService surveyService;
    private final PointService pointService;
    private final QuestionAnswerService answerService;

//    @PostMapping("/videoTest")
//    public boolean createVideo(@RequestPart(value = "video", required = false) MultipartFile file) throws IOException {
//        String baseDir = "C:\\Users\\82103\\Desktop\\etc";
//
//        String filepath = baseDir + "\\" +  file.getOriginalFilename(); //files[i].getOriginalFilename();
//        System.out.println(filepath);
//        file.transferTo(new File(filepath));
//            //surveyRepository.save(survey);
//        return true;
//    }

    @PostMapping("/survey") // 설문조사 등록
    public SuccessDto createSurvey(@RequestPart(value = "file", required = false) MultipartFile[] files,
                                             @RequestPart(value = "requestDto") SurveyRequestDto requestDto,
                                             @AuthenticationPrincipal Member member) throws Exception {
        if (member != null)
            return new SuccessDto(surveyService.registerSurvey(requestDto, files, member));
        else {
            return new SuccessDto(false);
        }
    }

    // 설문조사 응답하기
    // 설문조사 마감 임박 순
    // ( 종료 날짜가 같을 시 응답자 수 가 적은 설문조사 부터 정렬)
    @GetMapping("/surveys") // 모든 설문조사 나열
    public List<SurveyWidelyDto> getSurveys() {
        return surveyService.getSurveys();
    }

    @GetMapping("/surveys/end") // 종료된 설문조사
    public List<SurveyWidelyDto> getEndSurveys() {
        return surveyService.getEndSurveys();
    }

    @GetMapping("/survey/{surveyId}") // 해당 설문조사의 질문들 목록
    public Map<String, Object> getSurvey(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        return surveyService.getSurveyAndQuestions(surveyId, member.getId());
    }

    @PutMapping("/survey/{surveyId}") // 설문조사 생성자가 설문조사 종료할 때
    public SuccessDto updateSurvey(@PathVariable Long surveyId, @RequestBody SurveyUpdateDto updateDto, @AuthenticationPrincipal Member member) {
        if (member != null)
            if (surveyService.updateSurvey(surveyId, updateDto)) return new SuccessDto(true);
            else return new SuccessDto(false);
        else return new SuccessDto(false);
    }


    @DeleteMapping("/survey/{surveyId}")
    public SuccessDto deleteSurvey(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        if (member != null) {
            surveyService.deleteBySurveyId(surveyId);
            return new SuccessDto(true);
        }
        return new SuccessDto(false);
    }

    @PostMapping("/survey/{surveyId}") // answer 등록
    public Map<String, Object> createAnswer(@PathVariable Long surveyId, @RequestBody AnswerRequestDto requestDto, @AuthenticationPrincipal Member member){
        Map<String, Object> map = new HashMap<>();
        if (member != null) {
            if(answerService.findBySurveyIdAndAnswerMemberId(surveyId, member.getId())){
                map.put("success", false);
                map.put("message", "이미 답변한 설문조사입니다.");
            }
            else {
                int point = answerService.registerAnswer(surveyId, requestDto, member);
                map.put("point", pointService.getPoint(surveyId, member));
                map.put("success", true);
            }
        } else {
            map.put("success", false);
        }
        return map;
    }

    @GetMapping("/survey/{surveyId}/answer") // surveyId // 설문조사 응답 보기
    public Map<String, Object> getAnswer(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        Map<String, Object> map = new HashMap<>();

        if (member != null) {
            map.put("success", true);
            pointService.usePoint(member, surveyId);
            map.put("questions", surveyService.getQuestionTexts(surveyId)); // 질문 리스트
            map.put("summary", answerService.getAnswers(surveyId)); // 요약 보기
            map.put("ones", answerService.getOnes(surveyId));
        }
        else {
            map.put("success", false);
            map.put("message", "유효하지 않은 토큰");
        }
        return map;
    }

    @GetMapping("/question/{questionId}/answer/age-gender") // 나이/성별별 결과
    public Map<String, Object> ageAndGender(@PathVariable("questionId")  Long questionId, @AuthenticationPrincipal Member member){

        Map<String, Object> map = new HashMap<>();
        if (member != null) {
            List<QuestionRepoDto> list = answerService.getRepoList(questionId);
            Question q = answerService.getQuestionText(questionId);
            map.put("questionText", q.getQuestionText());
            map.put("choiceTexts", answerService.getChoiceTexts(q));
            map.put("gender", answerService.getGender(list, questionId)); // 성별별
            map.put("age", answerService.getAge(list, questionId)); // 나이별별
       }
        else {
            map.put("success", false);
            map.put("message", "유효하지 않은 토큰");
        }
        return map;
    }

    @GetMapping("/survey/answers")
    public List<Answer> getAnswers() {
        return answerService.getAnswers();
    }

    @GetMapping("/survey")
    public List<SurveyWidelyDto> getSearch(@RequestParam("search") String searchVal) {
        return surveyService.getSearch(searchVal);
    }

    @GetMapping("/survey/end") // 종료된 설문조사 search
    public List<SurveyWidelyDto> getSearchEnd(@RequestParam("search") String searchVal) {
        return surveyService.getSearchEnd(searchVal);
    }

    @GetMapping("/survey/{surveyId}/me") // surveyId // 설문조사 응답 보기
    public Map<String, Object> getAnswerByMember(@PathVariable Long surveyId, @AuthenticationPrincipal Member member) {
        Map<String, Object> map = new HashMap<>();
        if (member != null) {
            map.put("success",true);
            map.put("response",answerService.getQuestionAndAnswerByMemberId(surveyId, member.getId()));
        }
        else map.put("success", false);

        return map;
    }

    @GetMapping("/point")
    public Map<String, Object> getPoint(@AuthenticationPrincipal Member member){
        Map<String, Object> map  = new HashMap<>();
        if (member != null) {
            map.put("success",true);
            map.put("point", pointService.getMemberPoint(member.getId()));
        }
        else map.put("success", false);
        return map;
    }

    @GetMapping("/myPage")
    public Map<String, Object> myPage(@AuthenticationPrincipal Member member){
        Map<String, Object> map = new HashMap<>();
        if(member != null){
            map.put("success", true);
            map.put("surveyList", surveyService.getMyInfo(member.getId()));
            map.put("point", pointService.getMemberPoint(member.getId()));
        }
        else map.put("success", false);

        return map;
    }
}

