package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.ExcelUploadStatus;
import com.edubill.edubillApi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;
import java.util.Optional;

public interface ExcelUploadStatusRepository extends JpaRepository<ExcelUploadStatus, Long> {
    Optional<ExcelUploadStatus> findByYearMonthAndUser(YearMonth yearMonth, User user);

}
