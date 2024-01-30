package com.edubill.edubillApi.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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

    //    @Column(name = "user_email", unique = true, nullable = false)
    //    private String userEmail;

    //    @Column(name = "user_password", nullable = false)
    //    private String userPassword;

    // 생성메서드
    public User(String phoneNumber,String userName, UserRole userRole, String requestId) {
        this.phoneNumber = phoneNumber;
        this.userName = userName;
        this.userRole = userRole;
        this.authInfo = new AuthInfo(requestId);
    }
}