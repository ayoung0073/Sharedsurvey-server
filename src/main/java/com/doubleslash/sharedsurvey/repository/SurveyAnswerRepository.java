package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.dto.questionAndAnswer.QuestionRepoDto;
import com.doubleslash.sharedsurvey.domain.entity.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

    // 해당 설문조사 답변 여부
    Optional<SurveyAnswer> findBySurveyIdAndAnswerMemberId(Long surveyId, Long memberId);

    List<SurveyAnswer> findAllBySurveyId(Long surveyId);

    //@Query(value = "select questionId, answerText, count(answerText), gender, age from Answer inner join Member on Answer.writerId=Member.id where surveyId=?1 group by answerText;",nativeQuery = true)
    @Query(value = "select answerText, gender, age from Answer inner join Member on Answer.writerId=Member.id where questionId=?1", nativeQuery = true)
    List<Object[]> getAnswerMembers(Long questionId);

}
