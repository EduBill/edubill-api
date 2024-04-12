package com.edubill.edubillApi.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Schema(description = "사용자 존재 유무 확인 요청 dto")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExistUserRequestDto {

    @Schema(description = "requestId", type = "String", example = "5cdf2372-d3c2-42ac-b517-0bad88fcbbf7")
    @NotNull(message = "고유요청은 필수입니다.")
    private String requestId;

    @Schema(description = "phoneNumber", type = "String", example = "01012345678")
    @Pattern(regexp =  "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$",message = "-을 제외한 11자리 번호를 입력해주세요")
    private String phoneNumber;

}