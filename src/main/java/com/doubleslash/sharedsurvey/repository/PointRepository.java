package com.doubleslash.sharedsurvey.repository;

import com.doubleslash.sharedsurvey.domain.entity.Point;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointRepository extends JpaRepository<Point, Long> {
}
