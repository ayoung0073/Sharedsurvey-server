package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.*;
import com.doubleslash.sharedsurvey.domain.entity.Answer;
import com.doubleslash.sharedsurvey.domain.entity.Member;
import com.doubleslash.sharedsurvey.domain.entity.Question;
import com.doubleslash.sharedsurvey.domain.entity.Survey;
import com.doubleslash.sharedsurvey.repository.AnswerRepository;
import com.doubleslash.sharedsurvey.repository.MemberRepository;
import com.doubleslash.sharedsurvey.repository.QuestionRepository;
import com.doubleslash.sharedsurvey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public Long updateSurvey(Long id, SurveyUpdateDto updateDto){
        Survey survey = surveyRepository.findById(id).orElseThrow();
        survey.updateSurvey(updateDto);

        return id;
    }

    @Transactional
    public Survey registerSurvey(SurveyRequestDto requestDto){
        if(memberRepository.findById(requestDto.getWriterId()).isPresent()) {
            requestDto.setWriterId(requestDto.getWriterId());
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
        Survey survey = surveyRepository.findById(id).orElseThrow();
        List<Question> questions = questionRepository.findAllBySurveyId(id);

        Map<String, Object> map = new HashMap<>();
        map.put("surveyId",survey.getId());
        map.put("questions", questions);

        return map;
    }

    @Transactional
    public boolean registerAnswer(AnswerRequestDto requestDto) {

        Long writerId = requestDto.getWriterId();
        for (QuestionAnswerDto dto: requestDto.getAnswer()) {
            Answer answer = new Answer(writerId, dto.getQuestionId(), dto.getAnswerText());
            answerRepository.save(answer);
        }
        return true;
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
