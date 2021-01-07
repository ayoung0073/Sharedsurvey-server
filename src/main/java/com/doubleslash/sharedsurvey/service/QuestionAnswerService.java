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
            int result = answerRepository.answerSave(dto.getAnswerText(), dto.getQuestionId(), member.getId(), surveyId);
            if (result == 0) throw new IllegalArgumentException("답변 저장 실패");
        }

        surveyAnswerRepository.save(surveyAnswer);
        return survey.getPoint();
    }

    @Transactional(readOnly = true)
    public boolean findBySurveyIdAndAnswerMemberId(Long surveyId, Long memberId) {
        return surveyAnswerRepository.findBySurveyIdAndAnswerMemberId(surveyId, memberId).isPresent();
    }

    @Transactional(readOnly = true)
    public List<Object> getAnswers(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId).orElseThrow(
                () -> new IllegalArgumentException("해당 설문조사가 존재하지 않습니다")
        );
        List<Question> questions = survey.getQuestions();

        //List<Map<Object, Object>> list = new ArrayList<>();
        List<Answer> answers;
        List<Object> answerTexts = new ArrayList<>();
        Map<Object, Object> map;
        System.out.println(questions.size());
        for (Question question : questions) {
            if (question.getQuestionCategoryId() == 1 || question.getQuestionCategoryId() == 2) {
                map = new HashMap<>();
                Map<String, Integer> answerMap = new HashMap<>();
                answers = question.getAnswers();
                for (Answer a : answers) {
                    String answerText = a.getQuestion().getQuestionChoices().get(Integer.parseInt(a.getAnswerText())-1).getChoiceText();
                    if (answerMap.containsKey(answerText)) answerMap.put(answerText, answerMap.get(answerText) + 1);
                    else answerMap.put(answerText, 1);
                }
                map.put("answerChoice", answerMap);
                answerTexts.add(map);
            } else {
                answers = question.getAnswers();
                String[] answerArr = new String[answers.size()];
                int i = 0;
                for (Answer a : answers) {
                    answerArr[i++] = a.getAnswerText();
                }
                answerTexts.add(answerArr);
            }
            //list.add(map);
        }
        return answerTexts;
    }

    @Transactional(readOnly = true)
    public Map<Long, String[]> getOnes(Long surveyId) {
        List<SurveyAnswer> surveyAnswers = surveyAnswerRepository.findAllBySurveyId(surveyId);
        Map<Long, String[]> map = new HashMap<>();
        for (SurveyAnswer surveyAnswer : surveyAnswers) {
            List<Answer> answers = answerRepository.findAllByWriterIdAndSurveyId(surveyAnswer.getAnswerMember().getId(), surveyAnswer.getSurvey().getId());
            String[] answerList = new String[answers.size()];
            for(int i = 0; i < answerList.length; i++){
                Answer a = answers.get(i);
                if(a.getQuestion().getQuestionCategoryId() == 1 || a.getQuestion().getQuestionCategoryId() == 2 || a.getQuestion().getQuestionCategoryId() == 3)
                    answerList[i] = a.getQuestion().getQuestionChoices().get(Integer.parseInt(a.getAnswerText()) - 1).getChoiceText();
                else answerList[i] = a.getAnswerText();
            }
            map.put(surveyAnswer.getAnswerMember().getId(), answerList);
        }
        return map;
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
