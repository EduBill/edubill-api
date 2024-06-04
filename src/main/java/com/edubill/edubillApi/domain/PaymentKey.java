package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PaymentKey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_key_id")
    private Long id;

    @Column(name = "payment_key")
    private String paymentKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    public boolean matches(String otherPaymentKey) {
        return this.paymentKey.equals(otherPaymentKey);
    }
}
