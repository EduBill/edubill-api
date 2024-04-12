package com.edubill.edubillApi.user.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User extends BaseEntity {

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "user_role", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Column(name = "user_type", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Transient
    private AuthInfo authInfo;
    @Builder
    public User(String userId, String phoneNumber, String userName, String requestId, String verificationNumber, UserRole userRole) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.authInfo = new AuthInfo(requestId, verificationNumber);
        this.userRole = userRole;
    }

    public void updateUserName(String userName) {
        this.userName = userName;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}