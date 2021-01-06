package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.AnswerRequestDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionAnswerDto;
import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionAnswerResponseDto;
import com.doubleslash.sharedsurvey.domain.entity.*;
import com.doubleslash.sharedsurvey.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class QuestionAnswerService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;

    @Transactional
    public int registerAnswer(Long surveyId, AnswerRequestDto requestDto, Member member) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다."));
        survey.updateCount();
        surveyRepository.save(survey);
        SurveyAnswer surveyAnswer = new SurveyAnswer(survey, member);

        requestDto.setWriter(member);

        for (QuestionAnswerDto dto : requestDto.getAnswer()) {
            int result = answerRepository.answerSave(dto.getAnswerText(), dto.getQuestionId(),survey.getWriter().getId());
            if(result == 0) throw new IllegalArgumentException("답변 저장 실패");
        }

        surveyAnswerRepository.save(surveyAnswer);
        return survey.getPoint();
    }

    @Transactional(readOnly = true)
    public boolean findBySurveyIdAndAnswerMemberId(Long surveyId, Long memberId){
        return surveyAnswerRepository.findBySurveyIdAndAnswerMemberId(surveyId, memberId).isPresent();
    }

    @Transactional(readOnly = true)
    public List<Map<Object, Object>> getAnswers(Long surveyId){
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다")
        );
        List<Question> questions = survey.getQuestions();

        List<Map<Object, Object>> list = new ArrayList<>();
        List<Answer> answers;
        System.out.println(questions.size());
        for (Question question : questions) {
            Map<Object, Object> map = new HashMap<>();
            System.out.println(question.getQuestionText());
            answers = question.getAnswers();
            String[] answerArr = new String[answers.size()];
            int i = 0;
            for(Answer a: answers){
                answerArr[i++] = a.getAnswerText();
            }
            map.put("questionText", question.getQuestionText());
            map.put("answerText", answerArr);

            list.add(map);
        }
        return list;
    }

    @Transactional(readOnly = true)
    public List<QuestionAnswerResponseDto> getQuestionAndAnswerByMemberId(Long surveyId, Long memberId){
        List<Question> questions = getSurvey(surveyId);

        List<QuestionAnswerResponseDto> list = new ArrayList<>();
        for(Question q : questions){
            QuestionAnswerResponseDto dto = new QuestionAnswerResponseDto();
            dto.setQuestion(q.getQuestionText());
            dto.setQuestionCategoryId(q.getQuestionCategoryId());

            List<QuestionChoice> questionChoiceList = q.getQuestionChoices();
            String[] choiceTexts = new String[questionChoiceList.size()];
            for(int i = 0; i < questionChoiceList.size(); i++){
                choiceTexts[i] = questionChoiceList.get(i).getChoiceText();
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

    public List<Answer> getAnswers() {
        return answerRepository.findAll();
    }
}
