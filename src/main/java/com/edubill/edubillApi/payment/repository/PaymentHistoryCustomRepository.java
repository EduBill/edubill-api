package com.edubill.edubillApi.payment.repository;

import com.edubill.edubillApi.payment.domain.PaymentHistory;

import java.time.YearMonth;
import java.util.List;

public interface PaymentHistoryCustomRepository {
    List<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String userId, YearMonth yearMonth);

    long countPaidUserGroupsForUserInMonth(String userId, YearMonth yearMonth);
}
