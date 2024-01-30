package com.edubill.edubillApi.dto.user;

import com.edubill.edubillApi.domain.UserRole;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {

    private String userName;
    @Pattern(regexp =  "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$",message = "휴대폰 번호의 양식과 맞지 않습니다.")
    private String phoneNumber;
    private String requestId;
    private UserRole userRole;
}
