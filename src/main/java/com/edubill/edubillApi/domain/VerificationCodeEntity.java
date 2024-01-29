package com.edubill.edubillApi.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VerificationCodeEntity {

    @Id
    @Column(name = "verify_request_id")
    private String requestId;
    private String phoneNumber;
    private String VerificationCode;


}
