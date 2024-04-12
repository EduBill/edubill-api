package com.edubill.edubillApi.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthInfo {

    private String requestId;
    private String verificationNumber;

    public AuthInfo(String requestId, String verificationNumber) {
        this.requestId = requestId;
        this.verificationNumber = verificationNumber;
    }
}
