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

    @ManyToOne
    @JoinColumn(name = "academy_id")
    private Academy academy;

    @Column(name = "transaction_date_time")
    private String transactionDateTime; //거래일시

    @Column(name = "student_name")
    private String depositorName; // 입금자이름이 학생이름과 같은지 체크 필요

    @Column(name = "bank_name")
    private String bankName; // 은행이름

    @Column(name = "deposit_amount")
    private int depositAmount; // 입금액

    @Column(name = "memo")
    private String memo;  // 메모


    public PaymentInfo(String transactionDateTime, String depositorName, String bankName, int depositAmount, String memo) {
        this.transactionDateTime = transactionDateTime;
        this.depositorName = depositorName;
        this.bankName = bankName;
        this.depositAmount = depositAmount;
        this.memo = memo;
    }

    //연관관계 메서드
    public void setAcademy(Academy academy) {
        this.academy = academy;
        academy.getPaymentInfos().add(this);
    }
}
