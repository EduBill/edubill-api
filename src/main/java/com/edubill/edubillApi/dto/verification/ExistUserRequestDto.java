package com.edubill.edubillApi.dto.verification;

import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ExistUserRequestDto {

    @Pattern(regexp =  "^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$",message = "휴대폰 번호의 양식과 맞지 않습니다.")
    private String phoneNumber;
    private String requestId;
}