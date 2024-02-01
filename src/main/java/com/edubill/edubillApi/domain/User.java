package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "user_role", nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Transient
    private AuthInfo authInfo;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL) //user 삭제시 payment도 삭제
    private List<Payment> payments = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Account> accounts = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Invoice> invoices = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Academy> academies = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Student> students = new ArrayList<>();



    // 생성메서드
    public User(String phoneNumber,String userName, UserRole userRole, String requestId) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.userRole = userRole;
        this.authInfo = new AuthInfo(requestId);
    }
}