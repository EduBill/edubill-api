package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
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
    @JoinColumn(name = "student_id")
    private Student student;


    //==연관관계 메서드==//

    public void setStudent(Student student) {
        this.student = student;
        student.getPayments().add(this);
    }
}
