package com.edubill.edubillApi.repository;

import com.edubill.edubillApi.domain.PaymentHistory;
import com.edubill.edubillApi.domain.Student;
import com.edubill.edubillApi.domain.StudentPaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.YearMonth;

public interface StudentPaymentHistoryRepository extends JpaRepository<StudentPaymentHistory, Long>, StudentPaymentHistoryCustomRepository{

    Boolean existsByStudentAndYearMonth(Student student, String yearMonth);
    Boolean existsByPaymentHistoryAndYearMonth(PaymentHistory paymentHistory, String yearMonth);

}
