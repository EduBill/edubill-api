package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.dto.payment.PaymentHistoryDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;

@Getter
@Builder(toBuilder = true)
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

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private PaymentStatus paymentStatus = PaymentStatus.UNPAID; //납부확인 유무


    public static PaymentHistory toEntity(PaymentHistoryDto paymentHistoryDto, PaymentType paymentType) {
        return  PaymentHistory.builder()
                .depositDate(paymentHistoryDto.getDepositDate())
                .depositorName(paymentHistoryDto.getDepositorName())
                .bankName(paymentHistoryDto.getBankName())
                .paidAmount(paymentHistoryDto.getDepositAmount())
                .memo(paymentHistoryDto.getMemo())

                .paymentType(paymentType)
                .build();
    }
}
