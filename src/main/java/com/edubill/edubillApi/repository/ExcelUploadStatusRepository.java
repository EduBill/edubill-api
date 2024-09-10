package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.ExcelUploadStatus;
import com.edubill.edubillApi.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface ExcelUploadStatusRepository extends JpaRepository<ExcelUploadStatus, Long> {
    Optional<ExcelUploadStatus> findByYearMonthAndUser(String yearMonth, User user);

    void deleteAllByYearMonthAndUser(String yearMonth, User user);

    List<ExcelUploadStatus> findAllByUser(User user);
}
