package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.dto.payment.PaymentHistoryDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_history_id")
    private Long id;

    @Column(name = "deposit_date")
    private LocalDateTime depositDate; //거래일시

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

    @Enumerated(EnumType.STRING)
    private PaymentType paymentType; //거래방식

    public PaymentHistory(LocalDateTime depositDate, String depositorName, String bankName, int paidAmount, String memo, Long studentGroupId, PaymentType paymentType) {
        this.depositDate = depositDate;
        this.depositorName = depositorName;
        this.bankName = bankName;
        this.paidAmount = paidAmount;
        this.memo = memo;
        this.studentGroupId = studentGroupId;
        this.paymentType = paymentType;
    }

    public static PaymentHistory toEntity(PaymentHistoryDto paymentHistoryDto, Long studentGroupId, PaymentType paymentType) {
        return new PaymentHistory(
                paymentHistoryDto.getDepositDate(),
                paymentHistoryDto.getDepositorName(),
                paymentHistoryDto.getBankName(),
                paymentHistoryDto.getDepositAmount(),
                paymentHistoryDto.getMemo(),
                studentGroupId,
                paymentType
        );
    }
}
