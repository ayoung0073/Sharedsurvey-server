package com.doubleslash.sharedsurvey.service;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.*;
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
    private final QuestionChoiceRepository choiceRepository;

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
        Survey survey = surveyRepository.findById(surveyId).orElseGet(
                () -> {throw new IllegalArgumentException("해당 설문조사가 없습니다.");});

        List<Question> questions = survey.getQuestions();

        //List<Map<Object, Object>> list = new ArrayList<>();
        List<Answer> answers;
        List<Object> answerTexts = new ArrayList<>();
        Map<Object, Object> map;
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
                answerList[i] = a.getAnswerText();
            }
            map.put(surveyAnswer.getAnswerMember().getId(), answerList);
        }
        return map;
    }

    @Transactional(readOnly = true)
    public List<QuestionAnswerResponseDto> getQuestionAndAnswerByMemberId(Long surveyId, Long memberId){
        List<Question> questions = getQuestionsBySurvey(surveyId);

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

    public List<Question> getQuestionsBySurvey(Long surveyId){
        return questionRepository.findAllBySurveyId(surveyId);
    }

    public List<Answer> getAnswers() {
        return answerRepository.findAll();
    }

    public Question getQuestionText(Long questionId){
        return questionRepository.findById(questionId).orElseThrow(
                () -> new IllegalArgumentException("해당 질문이 없습니다.")
        );
    }

    public List<Map<String, GenderCount>> getGender(List<QuestionRepoDto> list, Long questionId){
        //List<Question> questions = getSurvey(questionId);
        // 성별로 구분하자
        Question question = questionRepository.findById(questionId).orElseGet(
                () -> {throw new IllegalArgumentException("해당 설문조사가 없습니다.");});

        List<Map<String, GenderCount>> retList = new ArrayList<>();
        Map<String, GenderCount> map;
        for(QuestionRepoDto dto: list){
            map = new HashMap<>();
            GenderCount genderCount = new GenderCount();
            if(!map.containsKey(dto.getAnswerText())){
                if(dto.isGender()) genderCount.setWoman(genderCount.getWoman() + 1);
                else genderCount.setMan(genderCount.getMan() + 1);
            }
            else{
                if(dto.isGender()) genderCount.setWoman(1);
                else genderCount.setMan(1);
            }
            map.put(choiceRepository.findAllByQuestionId(questionId).get(Integer.parseInt(dto.getAnswerText()) - 1).getChoiceText(), genderCount);

            retList.add(map);
        }

        return retList;
    }

    public List<Map<String, AgeCountDto>> getAge(List<QuestionRepoDto> list, Long questionId){
        //List<Question> questions = getSurvey(questionId);
        List<Map<String, AgeCountDto>> retList = new ArrayList<>();
        Map<String, AgeCountDto> map;
        for(QuestionRepoDto dto: list){
            map = new HashMap<>();
            AgeCountDto ageCountDto = new AgeCountDto();
            int age =  dto.getAge() / 10; // 10대, 20대 ~ -> 단위 10 -> 십의자리수 보기
            if(!map.containsKey(dto.getAnswerText())) {
                switch (age) {
                    case 1:
                        ageCountDto.setAge10(ageCountDto.getAge10() + 1);
                    case 2:
                        ageCountDto.setAge20(ageCountDto.getAge20() + 1);
                    case 3:
                        ageCountDto.setAge30(ageCountDto.getAge30() + 1);
                    case 4:
                        ageCountDto.setAge40(ageCountDto.getAge40() + 1);
                    case 5:
                        ageCountDto.setAge60(ageCountDto.getAge50() + 1);
                    case 6:
                        ageCountDto.setAge60(ageCountDto.getAge60() + 1);
                }
            }
            map.put(choiceRepository.findAllByQuestionId(questionId).get(Integer.parseInt(dto.getAnswerText()) - 1).getChoiceText(), ageCountDto);

            retList.add(map);
        }

        return retList;
    }

    public List<QuestionRepoDto> getRepoList(Long questionId) {
        //List<Question> questions = getSurvey(questionId);
        List<Object[]> writerList = surveyAnswerRepository.getAnswerMembers(questionId);

        List<QuestionRepoDto> list = new ArrayList<>();
        for (Object[] obj : writerList) {
            list.add(new QuestionRepoDto((String) obj[0], (boolean) obj[1], (int) obj[2]));
        }

        return list;
    }

    public String[] getChoiceTexts(Question q){
        List<QuestionChoice> list = q.getQuestionChoices();
        int size = list.size();
        String[] choiceTexts = new String[size];
        for(int i = 0; i < size; i++){
            choiceTexts[i] = list.get(i).getChoiceText();
        }
        return choiceTexts;
    }
}
