package com.edubill.edubillApi.payment.domain;

import com.edubill.edubillApi.domain.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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

    private LocalDate depositDate;

    private String depositor;
}
