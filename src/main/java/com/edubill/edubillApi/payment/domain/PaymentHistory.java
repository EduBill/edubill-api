package com.edubill.edubillApi.payment.domain;

import com.edubill.edubillApi.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
public class PaymentHistory extends BaseEntity {

    protected PaymentHistory() {

    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer paidAmount;

    private Long studentGroupId;

    private LocalDateTime depositDate;

    private String depositor;
}
