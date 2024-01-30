package com.edubill.edubillApi.dto.verification;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class VerificationRequestDto {

    private String enteredNumber; //인증번호
    private String requestId; //unique request

}
