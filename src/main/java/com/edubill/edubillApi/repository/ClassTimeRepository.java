package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.ClassTime;
import com.edubill.edubillApi.domain.Group;
import com.edubill.edubillApi.domain.enums.DayOfWeek;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalTime;
import java.util.List;

public interface ClassTimeRepository extends JpaRepository<ClassTime, Long> {
    List<ClassTime> findByGroupId(long groupId);
    void deleteByClassTimeId(Long classTimeId);
}
