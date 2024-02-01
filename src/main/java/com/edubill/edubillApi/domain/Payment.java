package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "PAYMENTS")
public class Payment extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status")
    private PaymentStatus paymentStatus; //완료, 미완료

    @Column(name = "total_payment_amount")
    private Integer totalPaymentAmount;

    @Column(name = "paid_amount")
    private Integer paidAmount;

    @Column(name = "difference")
    private Integer difference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    //==연관관계 메소드==//

    public void setUser(User user) {
        this.user = user;
        user.getPayments().add(this);
    }

    //==생성 메소드==//
    public static Payment createPayment(User user, Account account) {
        Payment payment = new Payment();
        payment.setPaymentStatus(PaymentStatus.UNPAID);
        return payment;
    }

}
