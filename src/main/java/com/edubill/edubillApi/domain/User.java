package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USERS")
public class User extends BaseEntity{

    @Id
    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "auth_role", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private AuthRole authRole;

    @Column(name = "user_type", columnDefinition = "varchar(50)")
    @Enumerated(EnumType.STRING)
    private UserType userType;

    @Transient
    private AuthInfo authInfo;
    @Builder
    public User(String userId, String phoneNumber, String userName, String requestId, AuthRole authRole) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.authInfo = new AuthInfo(requestId);
        this.authRole = authRole;
    }
}