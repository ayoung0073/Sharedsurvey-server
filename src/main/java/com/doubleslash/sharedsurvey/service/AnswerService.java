package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.AnswerRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionAnswerDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionAnswerResponseDto;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AnswerService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final QuestionChoiceRepository questionChoiceRepository;

    @Transactional
    public int registerAnswer(Long surveyId, AnswerRequestDto requestDto, Long memberId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));
        survey.updateCount();
        surveyRepository.save(survey);

        requestDto.setWriterId(memberId);
        for (QuestionAnswerDto dto : requestDto.getAnswer()) {
            Answer answer = new Answer(memberId, surveyId, dto.getQuestionId(), dto.getAnswerText());
            answerRepository.save(answer);
        }

        surveyAnswerRepository.save(new SurveyAnswer(surveyId, memberId));
        return survey.getPoint();
    }

    @Transactional
    public Map<Object, Object> getAnswers(Long surveyId){
        List<Question> questions = getSurvey(surveyId);

        Map<Object, Object> map = new HashMap<>();
        List<Answer> answers;
        for (Question question : questions) {
            answers = answerRepository.findAllByQuestionId(question.getId());
            String[] answerTexts = new String[answers.size()];

            for(int i = 0; i < answers.size(); i++){
                answerTexts[i] = answers.get(i).getAnswerText();
            }
            map.put(question.getQuestionText(), answerTexts);
        }
        return map;
    }

    @Transactional
    public List<QuestionAnswerResponseDto> getQuestionAndAnswerBymemberId(Long surveyId, Long memberId){
        List<Question> questions = getSurvey(surveyId);

        List<QuestionAnswerResponseDto> list = new ArrayList<>();
        for(Question q : questions){
            QuestionAnswerResponseDto dto = new QuestionAnswerResponseDto();
            dto.setQuestion(q.getQuestionText());
            dto.setQuestionCategoryId(q.getQuestionCategoryId());
            List<QuestionChoice> questionChoiceList = questionChoiceRepository.findAllByQuestionId(q.getId());
            //List<String> choiceTexts = new ArrayList<>();
            String[] choiceTexts = new String[questionChoiceList.size()];
            List<String> answers = new ArrayList<>();
            for(int i = 0; i < questionChoiceList.size(); i++){
                choiceTexts[i] = questionChoiceList.get(i).getChoiceText();
                //.add(answerList.get(i).getAnswerText());
            }
            dto.setChoiceTexts(choiceTexts);
            dto.setAnswer(answerRepository.findByQuestionIdAndWriterId(q.getId(), memberId).getAnswerText());
            list.add(dto);
        }
        return list;
    }

    public List<Question> getSurvey(Long surveyId){
        return questionRepository.findAllBySurveyId(surveyId);
    }
}
