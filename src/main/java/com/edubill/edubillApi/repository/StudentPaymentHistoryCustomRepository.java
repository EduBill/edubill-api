package com.edubill.edubillApi.repository;

import java.time.YearMonth;
import java.util.List;

public interface StudentPaymentHistoryCustomRepository {
    List<Long> findStudentIdsByUserIdAndYearMonth(String userId, YearMonth yearMonth);
}
