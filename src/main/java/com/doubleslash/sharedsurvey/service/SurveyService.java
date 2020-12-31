package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionUpdateDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.survey.SurveyUpdateDto;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
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

        requestDto.setWriterId(member.getId());
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
            s.setSurveyId(survey.getId());
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
                    QuestionChoice questionChoice = new QuestionChoice(survey.getId(), question.getId(), i+1, s.getChoiceTexts()[i]);
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
            questionRepository.save(question);
        }
        return true;
    }



    @Transactional
    public Map<String, Object> getSurveyAndQuestions(Long surveyId, Long memberId){
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));
        List<Question> questions = questionRepository.findAllBySurveyId(surveyId);
        Map<String, Object> map = new HashMap<>();
        List<QuestionChoice> choices = questionChoiceRepository.findAllBySurveyId(surveyId);

        if(surveyAnswerRepository.findBySurveyIdAndAnswerMemberId(surveyId, memberId).isPresent())
            map.put("answer", true);
        else map.put("answer", false);

        map.put("surveyId",survey.getId());
        map.put("questions", questions);
        map.put("choices", choices);

        return map;
    }
}
