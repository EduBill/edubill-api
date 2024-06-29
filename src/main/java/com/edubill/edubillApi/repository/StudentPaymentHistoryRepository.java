package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.StudentPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPaymentHistoryRepository extends JpaRepository<StudentPaymentHistory, Long> {
}
