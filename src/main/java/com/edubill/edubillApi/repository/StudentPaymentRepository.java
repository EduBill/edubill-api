package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.ExcelUploadStatus;
import com.edubill.edubillApi.domain.StudentPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentPaymentRepository extends JpaRepository<StudentPaymentHistory, Long> {
}
