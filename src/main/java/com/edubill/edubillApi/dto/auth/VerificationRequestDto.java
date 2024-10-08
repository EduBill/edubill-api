package com.edubill.edubillApi.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "인증번호 확인 요청 dto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VerificationRequestDto {

    @Schema(description = "requestId", type = "String", example = "5cdf2372-d3c2-42ac-b517-0bad88fcbbf7")
    @NotNull(message = "고유요청은 필수입니다.")
    private String requestId;

    @Schema(description = "verificationNumber", type = "String", example = "123456")
    @NotNull(message = "인증번호 입력은 필수입니다.")
    private String verificationNumber; //인증번호

    @Schema(description = "phoneNumber", type = "String", example = "01012345678")
    @NotNull(message = "휴대폰 번호는 필수입니다.")
    @Pattern(regexp =  "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$",message = "-을 제외한 11자리 번호를 입력해주세요")
    private String phoneNumber;
}