package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {

    // 해당 설문조사 답변 여부
    Optional<SurveyAnswer> findBySurveyIdAndAnswerMemberId(Long surveyId, Long memberId);

    List<SurveyAnswer> findAllBySurveyId(Long surveyId);
}
