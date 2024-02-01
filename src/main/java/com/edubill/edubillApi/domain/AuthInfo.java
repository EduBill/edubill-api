package com.edubill.edubillApi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AuthInfo {

    private String requestId;
    private String verificationNumber;

    public AuthInfo(String requestId) {
        this.requestId = requestId;
    }
}
