package com.edubill.edubillApi.dto.verification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CheckUserRequestDto {

    private String phoneNumber;
    private String requestId;
}
