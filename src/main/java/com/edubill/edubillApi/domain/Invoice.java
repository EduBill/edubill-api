package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "INVOICES")
public class Invoice extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "invoice_id")
    private Long id;

    @Column(name = "receiver_name")
    private String receiverName;

    @Column(name="receiver_phone_number")
    private String receiverPhoneNumber;

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @Lob // 큰 텍스트 사용
    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getInvoices().add(this);
    }
}
