package com.edubill.edubillApi.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ExcelPaymentInfo {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userName;
    private String userEmail;
    private String userId;
    private String userPhone;
    private String userType;

    public ExcelPaymentInfo(String userName, String userEmail, String userId, String userPhone, String userType) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userId = userId;
        this.userPhone = userPhone;
        this.userType = userType;
    }
}
