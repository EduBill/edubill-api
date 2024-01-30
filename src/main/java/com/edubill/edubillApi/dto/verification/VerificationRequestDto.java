package com.edubill.edubillApi.dto.verification;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VerificationRequestDto {

    private String verificationNumber; //인증번호
    private String requestId; //unique request

}
