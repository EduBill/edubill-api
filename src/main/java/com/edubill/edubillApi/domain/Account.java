package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "ACCOUNTS")
public class Account extends BaseEntity{

    @Id
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "bank_name")
    private BankName bankName; //은행이름

    @Column(name = "holder_name")
    private String holderName; //계좌주 -> user, holder가 일치할때만 등록가능

    @Enumerated(EnumType.STRING)
    @Column(name = "account_status")
    private AccountStatus accountStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    //==연관관계 메서드==//
    public void setUser(User user) {
        this.user = user;
        user.getAccounts().add(this);
    }

}
