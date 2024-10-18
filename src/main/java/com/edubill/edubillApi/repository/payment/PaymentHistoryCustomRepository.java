package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

public interface PaymentHistoryCustomRepository {

    Page<PaymentHistory> findPaymentHistoriesByYearMonthAndManagerId(String userId, YearMonth yearMonth, Pageable pageable);

    Page<PaymentHistory> findUnpaidHistoriesByYearMonthAndManagerId(String userId, YearMonth yearMonth, Pageable pageable);

    List<PaymentHistory> findPaymentHistoriesByUserIdAndYearMonthWithPaymentStatusPaid(String managerId, YearMonth yearMonth);

    List<PaymentHistory> findPaymentHistoriesByManagerIdAndYearMonthWithPaymentStatusUnPaid(String managerId, YearMonth yearMonth);

    List<PaymentHistory> findPaymentHistoriesByManagerIdAndYearMonth(String managerId, YearMonth yearMonth);

    long countPaidStudentsForUserInMonth(String userId, YearMonth yearMonth);

    long deleteByUserIdAndYearMonth(String userId, YearMonth yearMonth);

    Optional<PaymentHistory> findByDepositDateAndDepositorNameAndBankNameAndManagerId(LocalDateTime depositDate, String depositorName, String bankName, String userId);
}
