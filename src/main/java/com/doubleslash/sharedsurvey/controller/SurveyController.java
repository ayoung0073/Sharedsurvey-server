package com.doubleslash.sharedsurvey.controller;

import com.doubleslash.sharedsurvey.domain.dto.AnswerRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.entity.Answer;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.AnswerRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import com.doubleslash.sharedsurvey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
public class SurveyController {

    private final SurveyService surveyService;
    private final SurveyRepository surveyRepository;
    private final AnswerRepository answerRepository;

    @PostMapping("/survey")
    public Survey createSurvey(@RequestBody SurveyRequestDto requestDto){
        return surveyService.registerSurvey(requestDto);
    }

    @GetMapping("/surveys")
    public List<Survey> getSurveys(){
        return surveyRepository.findAll();
    }

    @GetMapping("/survey/{id}")
    public Map<String, Object> getSurvey(@PathVariable Long id){
        return surveyService.getSurveyAndQuestions(id);
    }

    @PutMapping("/survey/{id}")
    public boolean updateSurvey(@PathVariable Long id, @RequestBody SurveyUpdateDto updateDto){
        if(surveyService.updateSurvey(id,updateDto)) return true;
        else return false;
    }

    @PostMapping("/survey/answer")
    public boolean createAnswer(@RequestBody AnswerRequestDto requestDto){
        return surveyService.registerAnswer(requestDto);
    }

    @GetMapping("/survey/answer/{id}") // surveyId
    public Map<Object, Object> getAnswer(@PathVariable Long id){
        return surveyService.getAnswers(id);
    }

    @GetMapping("/survey/answers")
    public List<Answer> getAnswers(){
        return answerRepository.findAll();
    }

}
