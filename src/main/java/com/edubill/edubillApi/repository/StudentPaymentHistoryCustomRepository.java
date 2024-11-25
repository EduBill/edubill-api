package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.StudentPaymentHistory;

import java.time.YearMonth;
import java.util.List;

public interface StudentPaymentHistoryCustomRepository {
    List<Long> findStudentIdsByUserIdAndYearMonth(String userId, YearMonth yearMonth);
}
