package com.edubill.edubillApi.payment.repository;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.YearMonth;
import java.util.List;

public interface PaymentHistoryCustomRepository {

    Page<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String userId, YearMonth yearMonth, Pageable pageable);

    List<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String managerId, YearMonth yearMonth);

    long countPaidUserGroupsForUserInMonth(String userId, YearMonth yearMonth);
}
