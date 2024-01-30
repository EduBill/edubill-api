package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_number")
    private Account account;

//    @Column(name = "payment_date")
//    private LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus; //SUCCESS, PENDING, FAILURE

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod; //CREDIT_CARD, BANK_TRANSFER, NAVER_PAY, KAKAO_PAY


    //==연관관계 메소드==//
    public void setUser(User user) {
        this.user = user;
        user.getPayments().add(this);
    }

    //==생성 메소드==//
    public static Payment createPayment(User user, Account account) {
        Payment payment = new Payment();
        payment.setPaymentMethod(PaymentMethod.BANK_TRANSFER); // 수정 필요
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        return payment;
    }

}
