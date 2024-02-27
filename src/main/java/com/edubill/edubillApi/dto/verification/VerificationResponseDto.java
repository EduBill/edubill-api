package com.edubill.edubillApi.dto.verification;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Schema(description = "인증번호 발송 완료 응답 dto")
@AllArgsConstructor
@Getter
public class VerificationResponseDto {

    @Schema(description = "requestId", type = "String", example = "5cdf2372-d3c2-42ac-b517-0bad88fcbbf7")
    private String requestId;
    @Schema(description = "verificationNumber", type = "String", example = "123456")
    private String verificationNumber; //인증번호
}