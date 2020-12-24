package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.*;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyWriterRepository surveyWriterRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;

    @Transactional
    public boolean registerSurvey(SurveyRequestDto requestDto, MultipartFile[] files) throws IOException {
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> optional = memberRepository.findByMemberId(user.getName());

        String baseDir = "C:\\Users\\82103\\IdeaProjects\\sharedsurvey\\src\\main\\resources\\static\\uploadFiles";
        String filePath = "";

        int i = 0;

        if(optional.isPresent()){
            requestDto.setWriterId(optional.get().getId());
            Survey survey = new Survey(requestDto);
            surveyRepository.save(survey);
            surveyWriterRepository.save(new SurveyWriter(survey.getId(),optional.get().getId()));
            List<QuestionRequestDto> questions = requestDto.getQuestions();
            for (QuestionRequestDto s : questions) {
                s.setSurveyId(survey.getId());
                Question question = questionRepository.save(new Question(s));
                if(s.isExistFile()){
                    filePath = baseDir + "\\" + files[i].getOriginalFilename();
                    files[i].transferTo(new File(filePath));

                    question.setFilename(files[i++].getOriginalFilename());
                    questionRepository.save(question);
                }
            }
            return true;
        }
        return false;
    }


    @Transactional
    public boolean updateSurvey(Long id, SurveyUpdateDto updateDto){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        Survey survey = surveyRepository.findById(id).get();
        if(survey.getWriter() == memberRepository.findByMemberId(user.getName()).get().getId()) {
            survey.updateSurvey(updateDto);
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
            Answer answer = new Answer(requestDto.getWriterId(), dto.getQuestionId(), dto.getAnswerText());
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
        return map;
    }

}
