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
public class Account extends BaseEntity{

    @Id
    @Column(name = "account_number", unique = true, nullable = false)
    private String accountNumber;

    @Column(name = "bank_name")
    private String bankName; //은행이름

    @Column(name = "holder_name")
    private String holderName; //계좌주 -> user, holder가 일치할때만 등록가능

    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

//    @OneToMany(mappedBy = "account", cascade = CascadeType.REMOVE, orphanRemoval = true)
//    private List<Payment> payments = new ArrayList<>();



}
