package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentKeyRepository extends JpaRepository<PaymentKey, Long> {
}
