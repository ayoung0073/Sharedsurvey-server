package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.Survey;
import net.bytebuddy.TypeCache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {

    // 종료된 설문조사 리스트
    @Query(value = "select * from Survey where date(endDate) < date(now()) order by endDate, responseCount;", nativeQuery = true)
    List<Survey> findAllByOrderByEndDateAndResponseCount();

    // 현재 진행 중인 설문조사 리스트
    @Query(value = "select * from Survey where date(endDate) > date(now()) order by endDate, responseCount;", nativeQuery = true)
    List<Survey> findAllByOrderByEndDateAndResponseCountEndDateAfter();


    // "나의" 종료된 설문조사 리스트
    @Query(value = "select * from Survey where date(endDate) < date(now()) AND memberId = ?1 order by endDate, responseCount;", nativeQuery = true)
    List<Survey> findAllByEndDateBeforeAndWriterOrderByEndDate(Long writerId);

    // "나의" 현재 진행 중인 설문조사 리스트
    @Query(value = "select * from Survey where date(endDate) > date(now()) AND memberId = ?1 order by endDate, responseCount;", nativeQuery = true)
    List<Survey> findAllByEndDateAfterAndWriterOrderByEndDate(Long writerId);


    // search
    List<Survey> findAllByEndDateAfterAndNameContainingOrderByEndDate(Date date,String searchVal);

    List<Survey> findAllByEndDateBeforeAndNameContainingOrderByEndDate(Date date,String searchVal);
}
