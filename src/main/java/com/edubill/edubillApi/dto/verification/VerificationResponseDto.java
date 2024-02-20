package com.edubill.edubillApi.dto.verification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VerificationResponseDto {

    private String requestId; //unique request
    private String verificationNumber; //인증번호
}