package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.QuestionChoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionChoiceRepository extends JpaRepository<QuestionChoice, Long> {
    List<QuestionChoice> findAllBySurveyId(Long surveyId);
    List<QuestionChoice> findAllByQuestionId(Long questionId);

}
