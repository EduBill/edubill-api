package com.edubill.edubillApi.dto.user;

import com.edubill.edubillApi.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Schema(description = "회원가입 요청 dto")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequestDto {

    @Schema(description = "requestId", type = "String", example = "5cdf2372-d3c2-42ac-b517-0bad88fcbbf7")
    @NotNull(message = "고유요청은 필수입니다.")
    private String requestId;

    @Schema(description = "userName", type = "String", example = "홍길동")
    @NotNull(message = "유저 이름은 필수입니다.")
    private String userName;

    @Schema(description = "phoneNumber", type = "String", example = "01012345678")
    @NotNull(message = "휴대폰 번호는 필수입니다.")
    @Pattern(regexp =  "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$",message = "-을 제외한 11자리 번호를 입력해주세요")
    private String phoneNumber;


//    @NotNull(message = "권한 입력은 필수입니다.")
//    private UserRole userRole;
}