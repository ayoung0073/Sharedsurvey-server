package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.Survey;
import net.bytebuddy.TypeCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Date;
import java.util.List;

public interface SurveyRepository extends JpaRepository<Survey, Long> {

    // 종료
    @Query(value = "select * from survey where date(end_date) < date(now()) order by end_date, response_count;", nativeQuery = true)
    List<Survey> findAllByOrderByEndDateAndResponseCount();

    @Query(value = "select * from survey where date(end_date) > date(now()) order by end_date, response_count;", nativeQuery = true)
    List<Survey> findAllByOrderByEndDateAndResponseCountEndDateAfter();

}
