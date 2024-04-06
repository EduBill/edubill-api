package com.edubill.edubillApi.payment.repository;

import com.edubill.edubillApi.payment.domain.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long>, PaymentHistoryCustomRepository {
}
