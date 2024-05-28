package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long>, PaymentHistoryCustomRepository {
}
