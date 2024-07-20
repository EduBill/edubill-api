package com.edubill.edubillApi.domain;

import com.edubill.edubillApi.domain.enums.AuthRole;
import com.edubill.edubillApi.domain.enums.UserType;
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
}