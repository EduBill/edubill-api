package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.parameters.P;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "STUDENET_PAYMENTHISTORY", uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "paymentHistory_id"}))
public class StudentPaymentHistory extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_payment_history_id")
    private Long studentPaymentHistoryId;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "student_id", insertable = false, updatable = false)
    private Student student;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "paymentHistory_id", insertable = false, updatable = false)
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

    @Builder
    public StudentPaymentHistory(Student student, PaymentHistory paymentHistory, String yearMonth)
    {
        this.student = student;
        this.paymentHistory = paymentHistory;
        this.yearMonth = yearMonth;
    }
}
