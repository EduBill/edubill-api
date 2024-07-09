package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.StudentPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;

public interface StudentPaymentHistoryRepository extends JpaRepository<StudentPaymentHistory, Long>, StudentPaymentHistoryCustomRepository{
}
