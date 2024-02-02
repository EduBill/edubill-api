package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Transient
    private AuthInfo authInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Academy> academies = new ArrayList<>();


    public User(String phoneNumber,String userName, UserRole userRole, String requestId) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.userRole = userRole;
        this.authInfo = new AuthInfo(requestId);
    }
}