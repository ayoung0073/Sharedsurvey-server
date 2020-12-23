package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.*;
import com.doubleslash.sharedsurvey.domain.entity.Answer;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Question;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyWriterRepository surveyWriterRepository;


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
    public Survey registerSurvey(SurveyRequestDto requestDto){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        Optional<Member> optional = memberRepository.findByMemberId(user.getName());
        if(optional.isPresent()){
            requestDto.setWriterId(optional.get().getId());
            Survey survey = new Survey(requestDto);
            surveyRepository.save(survey);
            List<QuestionRequestDto> questions = requestDto.getQuestions();
            for (QuestionRequestDto s : questions) {
                s.setSurveyId(survey.getId());
                Question question = new Question(s);
                questionRepository.save(question);
            }
            return survey;
        }
        return null;
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
    public boolean registerAnswer(AnswerRequestDto requestDto) {

        Authentication user = SecurityContextHolder.getContext().getAuthentication();

        Optional<Member> optional = memberRepository.findByMemberId(user.getName());
        if(!optional.isPresent()) {
            System.out.println(optional.get().getId());
            requestDto.setWriterId(optional.get().getId());
            for (QuestionAnswerDto dto : requestDto.getAnswer()) {
                Answer answer = new Answer(requestDto.getWriterId(), dto.getQuestionId(), dto.getAnswerText());
                answerRepository.save(answer);
            }
            return true;
        }
        else return false;
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
