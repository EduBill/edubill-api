package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_info_id")
    private Long id;

    @Column(name = "transaction_date_time")
    private LocalDateTime transactionDateTime; //거래일시

    @Column(name = "student_name")
    private String studentName; // 학생이름

    @Column(name = "bank_name")
    private String bankName; // 은행이름

    @Column(name = "deposit_amount")
    private int depositAmount; // 입금액

    @Column(name = "memo")
    private String memo;  // 메모


    public PaymentInfo(LocalDateTime transactionDateTime, String studentName, String bankName, int depositAmount, String memo) {
        this.transactionDateTime = transactionDateTime;
        this.studentName = studentName;
        this.bankName = bankName;
        this.depositAmount = depositAmount;
        this.memo = memo;
    }
}
