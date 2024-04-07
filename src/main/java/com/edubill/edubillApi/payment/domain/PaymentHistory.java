package com.edubill.edubillApi.payment.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long id;

    @Column(name = "deposit_date")
    private LocalDate depositDate; //거래일시

    @Column(name = "depositor_name")
    private String depositorName; // 입금자이름이 학생이름과 같은지 체크 필요

    @Column(name = "bank_name")
    private String bankName; // 은행이름

    @Column(name = "paid_amount")
    private Integer paidAmount; // 입금액

    @Column(name = "memo")
    private String memo;  // 메모

    @Column(name = "student_group_id")  // 외래 키
    private Long studentGroupId;

    public PaymentHistory(LocalDate depositDate, String depositorName, String bankName, int paidAmount, String memo, Long studentGroupId) {
        this.depositDate = depositDate;
        this.depositorName = depositorName;
        this.bankName = bankName;
        this.paidAmount = paidAmount;
        this.memo = memo;
        this.studentGroupId = studentGroupId;
    }
}
