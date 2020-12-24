package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.SurveyAnswer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyAnswerRepository extends JpaRepository<SurveyAnswer, Long> {
}
