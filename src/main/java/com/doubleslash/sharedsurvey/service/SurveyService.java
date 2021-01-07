package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.config.security.ApplicationYmlRead;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionUpdateDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyWidelyDto;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.criteria.CriteriaBuilder;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

@RequiredArgsConstructor
@Service
@PropertySource("classpath:application.yml")
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyWriterRepository surveyWriterRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final QuestionChoiceRepository questionChoiceRepository;

    private final FileService fileService;
    private final ApplicationYmlRead applicationYmlRead;

    @Transactional
    public boolean registerSurvey(SurveyRequestDto requestDto, MultipartFile[] files, Member member) throws Exception {
        //String baseDir = SharedsurveyApplication.class.getResource("").getPath() + "..\\..\\..\\..\\..\\..\\resources\\main\\static\\file";
        String baseDir = applicationYmlRead.getBase_dir();
        String filePath;
        String filename;

        requestDto.setWriter(member);

        Survey survey = surveyRepository.save(new Survey(requestDto));
        int i = 0;
        if(survey.isExistFile()) {
            fileService.saveSurveyFile(survey, files);
            i++;
        }


        List<QuestionRequestDto> questions = new ArrayList<>();
        questions = requestDto.getQuestions();

        System.out.println(questions.get(0).getChoiceTexts()[2]);

        fileService.saveQuestionSave(survey, questions, files, i); // i: 파일s 인덱스

        surveyWriterRepository.save(new SurveyWriter(survey.getId(),member.getId()));

        return true;
    }

    @Transactional
    public boolean updateSurvey(Long id, SurveyUpdateDto updateDto){
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));
        survey.updateSurvey(updateDto);

        List<QuestionUpdateDto> questions = updateDto.getQuestions();
        for (QuestionUpdateDto s : questions) {
            Question question = questionRepository.findById(s.getQuestionId()).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));
            question.updateQuestion(s);
        }
        return true;
    }

    @Transactional
    public void deleteBySurveyId(Long surveyId){
        surveyRepository.deleteById(surveyId);
    }

    public List<SurveyWidelyDto> getSurveys(){
        List<SurveyWidelyDto> list = new ArrayList<>();
        for(Survey s: surveyRepository.findAllByOrderByEndDateAndResponseCountEndDateAfter()){
            list.add(new SurveyWidelyDto(s));
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<SurveyWidelyDto> getEndSurveys(){
        List<SurveyWidelyDto> list = new ArrayList<>();
        for(Survey s: surveyRepository.findAllByOrderByEndDateAndResponseCount()){
            list.add(new SurveyWidelyDto(s));
        }
        return list;
    }

    @Transactional(readOnly = true)
    public Map<String, Object> getSurveyAndQuestions(Long surveyId, Long memberId){
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));

        List<Question> questions = survey.getQuestions();

        Map<String, Object> map = new HashMap<>();

        if(surveyAnswerRepository.findBySurveyIdAndAnswerMemberId(surveyId, memberId).isPresent())
            map.put("answer", true);
        else map.put("answer", false);

        map.put("survey",survey);

        return map;
    }

    @Transactional(readOnly = true)
    public Map<String, String[]> getQuestionTexts(Long surveyId) { // 질문과, 질문카테고리
        Map<String, String[]> map = new HashMap<>();
        List<Question> questions = questionRepository.findAllBySurveyId(surveyId);
        String[] questionTexts = new String[questions.size()];
        for(int i = 0; i < questions.size(); i++){
            Question q = questions.get(i);
            map.put(q.getQuestionText(), q.getChoices());
        }
        return map;
    }

    @Transactional(readOnly = true)
    public Map<String, List<SurveyWidelyDto>> getMyInfo(Long memberId) {
        Map<String, List<SurveyWidelyDto>> map = new HashMap<>();
        List<Survey> list = new ArrayList<>();
        list = surveyRepository.findAllByEndDateBeforeAndWriterOrderByEndDate(memberId);
        List<SurveyWidelyDto> endList = new ArrayList<>();
        List<SurveyWidelyDto> progressList = new ArrayList<>();

        for (Survey s : list) {
            endList.add(new SurveyWidelyDto(s));
            System.out.println(s.getName());
        }
        System.out.println(endList.size());
        map.put("end", endList);
        list = surveyRepository.findAllByEndDateAfterAndWriterOrderByEndDate(memberId);
        for (Survey s : list) {
            progressList.add(new SurveyWidelyDto(s));
            System.out.println(s.getName());
        }
        System.out.println(progressList.size());
        map.put("progress", progressList);

        return  map;
    }

    @Transactional(readOnly = true)
    public List<Survey> getSearchEnd(String searchVal) {
        return surveyRepository.findAllByEndDateBeforeAndNameContainingOrderByEndDate(new Date(), searchVal);
    }

    @Transactional(readOnly = true)
    public List<Survey> getSearch(String searchVal) {
        return surveyRepository.findAllByEndDateAfterAndNameContainingOrderByEndDate(new Date(), searchVal);
    }
}
