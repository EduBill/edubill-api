package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.parameters.P;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder(toBuilder = true)
@Table(name = "STUDENT_PAYMENT_HISTORY", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "paymentHistory_id"}))
public class StudentPaymentHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_payment_history_id")
    private Long studentPaymentHistoryId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id")
    private Student student;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "paymentHistory_id")
    private PaymentHistory paymentHistory;

    @Column(name = "year_month_str")
    private String yearMonth;

    //==연관관계 메서드==//
    public void setStudent(Student student) {
        this.student = student;
        student.getStudentPaymentHistories().add(this);
    }

    public void setPaymentHistory(PaymentHistory paymentHistory) {
        this.paymentHistory = paymentHistory;
        paymentHistory.toBuilder().studentPaymentHistory(this);
    }
}
