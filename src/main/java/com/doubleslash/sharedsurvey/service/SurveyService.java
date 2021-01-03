package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionUpdateDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyWidelyDto;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyWriterRepository surveyWriterRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final QuestionChoiceRepository questionChoiceRepository;

    @Transactional
    public boolean registerSurvey(SurveyRequestDto requestDto, MultipartFile[] files, Member member) throws IOException {

        //String baseDir = SharedsurveyApplication.class.getResource("").getPath() + "..\\..\\..\\..\\..\\..\\resources\\main\\static\\file";
        String baseDir = "/tmp/tomcat.8080.9056073029680764243/work/Tomcat/localhost/ROOT/";

        String filePath;
        String filename;

        int i = 0;

        requestDto.setWriter(member);

        System.out.println(requestDto.getCategory());
        System.out.println(requestDto.getDescription());
        System.out.println(requestDto.getWriter());
        System.out.println(requestDto.getStartDate());
        Survey survey = surveyRepository.save(new Survey(requestDto));
        
        if(survey.isExistFile()){
            filename = files[i].getOriginalFilename();
            assert filename != null;
            if(filename.split("\\.")[1].equalsIgnoreCase("png")) {
                filePath = baseDir + "/" + "thumbnail" + survey.getId() + ".png";//files[i].getOriginalFilename();
                survey.setFilename("thumbnail" + survey.getId() + ".png");
                files[i++].transferTo(new File(filePath));
                surveyRepository.save(survey);
            }
            else if(filename.split("\\.")[1].equalsIgnoreCase("jpg") || (filename.split("\\.")[1].equalsIgnoreCase("jpeg"))){
                filePath = baseDir + "/" + "thumbnail" + survey.getId() + ".jpg";//files[i].getOriginalFilename();
                survey.setFilename("thumbnail" + survey.getId() + ".jpg");
                files[i++].transferTo(new File(filePath));
                surveyRepository.save(survey);
            }
        }

        surveyWriterRepository.save(new SurveyWriter(survey.getId(),member.getId()));
        List<QuestionRequestDto> questions = requestDto.getQuestions();
        for (QuestionRequestDto s : questions) {
            s.setSurvey(survey);
            Question question = questionRepository.save(new Question(s));
            if(s.isExistFile()){ // .png, .jpg, .jpeg 만 가능
                filename = files[i].getOriginalFilename();
                assert filename != null;
                if(filename.split("\\.")[1].equalsIgnoreCase("png")) {
                    filePath = baseDir + "/" + question.getId() + ".png";//files[i].getOriginalFilename();
                    question.setFilename(question.getId() + ".png");
                    files[i++].transferTo(new File(filePath));
                    questionRepository.save(question);
                }
                else if(filename.split("\\.")[1].equalsIgnoreCase("jpg") || (filename.split("\\.")[1].equalsIgnoreCase("jpeg"))){
                    filePath = baseDir + "/" + question.getId() + ".jpg";//files[i].getOriginalFilename();
                    question.setFilename(question.getId() + ".jpg");
                    files[i++].transferTo(new File(filePath));
                    questionRepository.save(question);
                }
            }
            if(s.getQuestionCategoryId() == 1 || s.getQuestionCategoryId() == 2 || s.getQuestionCategoryId() == 3){
                // 질문 카테고리가 객관식, 체크박스, 드롭다운일 때
                for(i = 0; i < s.getChoiceTexts().length; i++){
                    QuestionChoice questionChoice = new QuestionChoice(survey, question, s.getChoiceTexts()[i]);
                    questionChoiceRepository.save(questionChoice);
                }
            }
        }
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
//            questionRepository.save(question);
        }
        return true;
    }

    public List<SurveyWidelyDto> getSurveys(){
        List<SurveyWidelyDto> list = new ArrayList<>();
        for(Survey s: surveyRepository.findAllByOrderByEndDateAndResponseCountEndDateAfter()){
            list.add(new SurveyWidelyDto(s));
        }
        return list;
    }

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

        //List<Question> questions = questionRepository.findAllBySurveyId(surveyId);
        List<Question> questions = survey.getQuestions();

        Map<String, Object> map = new HashMap<>();

//        List<QuestionChoice> choices = questionChoiceRepository.findAllBySurveyId(surveyId);

        //List<QuestionChoice> choices = questionChoiceRepository.findAllBySurveyId(surveyId);


        if(surveyAnswerRepository.findBySurveyIdAndAnswerMemberId(surveyId, memberId).isPresent())
            map.put("answer", true);
        else map.put("answer", false);

        map.put("survey",survey);
        //map.put("choices", choices);

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
}
