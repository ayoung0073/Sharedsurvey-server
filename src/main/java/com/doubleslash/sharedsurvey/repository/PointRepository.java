package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Long> {
    List<Point> findAllByMemberId(Long memberId);
}
