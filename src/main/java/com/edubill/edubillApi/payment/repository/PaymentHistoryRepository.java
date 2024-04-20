package com.edubill.edubillApi.payment.repository;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long>, PaymentHistoryCustomRepository {
}
