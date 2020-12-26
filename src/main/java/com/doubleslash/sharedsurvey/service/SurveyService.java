package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.SharedsurveyApplication;
import com.doubleslash.sharedsurvey.domain.dto.*;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyWriterRepository surveyWriterRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;

    @Transactional
    public boolean registerSurvey(SurveyRequestDto requestDto, MultipartFile[] files, Member member) throws IOException {

        String baseDir = SharedsurveyApplication.class.getResource("").getPath() + "..\\..\\..\\..\\..\\..\\resources\\main\\static\\file";
        String filePath = "";

        int i = 0;

        requestDto.setWriterId(member.getId());
        Survey survey = new Survey(requestDto);
        surveyRepository.save(survey);
        surveyWriterRepository.save(new SurveyWriter(survey.getId(),member.getId()));
        List<QuestionRequestDto> questions = requestDto.getQuestions();
        for (QuestionRequestDto s : questions) {
            s.setSurveyId(survey.getId());
            Question question = questionRepository.save(new Question(s));
            String filename = "";
            if(s.isExistFile()){ // .png, .jpg, .jpeg 만 가능
                filename = files[i].getOriginalFilename();
                assert filename != null;
                if(filename.split("\\.")[1].equalsIgnoreCase("png")) {
                    filePath = baseDir + "\\" + question.getId() + ".png";//files[i].getOriginalFilename();
                    question.setFilename(question.getId() + ".png");
                    files[i++].transferTo(new File(filePath));
                    questionRepository.save(question);
                }
                else if(filename.split("\\.")[1].equalsIgnoreCase("jpg") || (filename.split("\\.")[1].equalsIgnoreCase("jpeg"))){
                    filePath = baseDir + "\\" + question.getId() + ".jpg";//files[i].getOriginalFilename();
                    question.setFilename(question.getId() + ".jpg");
                    files[i++].transferTo(new File(filePath));
                    questionRepository.save(question);
                }
            }
        }
        return true;
    }


    @Transactional
    public boolean updateSurvey(Long id, boolean state, String memberId){
        Survey survey = surveyRepository.findById(id).get();
        if(survey.getWriter() == memberRepository.findByMemberId(memberId).get().getId()) {
            survey.updateSurvey(state);
            return true;
        }
        else return false;
    }

    @Transactional
    public boolean registerAnswer(Long surveyId, AnswerRequestDto requestDto, Long memberId) {

        Survey survey = surveyRepository.findById(surveyId).get();
        survey.updateCount();
        surveyRepository.save(survey);

        requestDto.setWriterId(memberId);
        for (QuestionAnswerDto dto : requestDto.getAnswer()) {
            Answer answer = new Answer(memberId, dto.getQuestionId(), dto.getAnswerText());
            answerRepository.save(answer);
        }

        surveyAnswerRepository.save(new SurveyAnswer(surveyId, memberId));
        return true;
    }


    @Transactional
    public Map<String, Object> getSurveyAndQuestions(Long id){
        Survey survey = surveyRepository.findById(id).get();
        List<Question> questions = questionRepository.findAllBySurveyId(id);

        Map<String, Object> map = new HashMap<>();
        map.put("surveyId",survey.getId());
        map.put("questions", questions);

        return map;
    }


    @Transactional
    public Map<Object, Object> getAnswers(Long surveyId){
        List<Question> questions = questionRepository.findAllBySurveyId(surveyId);
        Map<Object, Object> map = new HashMap<>();

        Long questionId = 0L;
        for(int i = 0; i < questions.size(); i++){
            questionId = questions.get(i).getId();
            map.put(questionId, answerRepository.findAllByQuestionId(questionId));
        }
        map.put("success", true);
        return map;
    }

}
