package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long>, PaymentHistoryCustomRepository {
}
