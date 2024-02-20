package com.edubill.edubillApi.dto.verification;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VerificationRequestDto {

    @NotNull(message = "고유요청은 필수입니다.")
    private String requestId; //unique request
    @NotNull(message = "인증번호 입력은 필수입니다.")
    private String verificationNumber; //인증번호
}