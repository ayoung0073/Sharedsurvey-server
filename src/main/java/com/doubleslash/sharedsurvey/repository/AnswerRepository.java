package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.Answer;
import org.graalvm.compiler.nodes.extended.ValueAnchorNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Answer findByQuestionIdAndWriterId(Long questionId, Long writerId);
    List<Answer> findAllByQuestionId(Long id);

    @Modifying
    @Query(value = "INSERT INTO Answer(answerText, questionId, writerId) VALUES (?1, ?2, ?3)", nativeQuery = true)
    int answerSave(String answerText, Long questionId, Long writerId);

}
