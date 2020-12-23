package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.SurveyWriter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyWriterRepository extends JpaRepository<SurveyWriter, Long> {
}
