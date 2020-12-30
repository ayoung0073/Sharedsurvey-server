package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Answer findByQuestionIdAndWriterId(Long questionId, Long writerId);
    List<Answer> findAllByQuestionId(Long id);

    List<Answer> findAllBySurveyId(Long id);

}
