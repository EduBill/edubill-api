package com.edubill.edubillApi.repository.payment;

import com.edubill.edubillApi.domain.PaymentKey;
import com.edubill.edubillApi.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentKeyRepository extends JpaRepository<PaymentKey, Long> {
    List<PaymentKey> findAllByStudent(Student student);
}
