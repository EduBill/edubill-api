package com.edubill.edubillApi.dto.user;

import com.edubill.edubillApi.jwt.JwtToken;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Schema(description = "로그인 응답 dto")
@Getter
@AllArgsConstructor
public class LoginResponseDto {

    private JwtToken jwtToken;
    private UserDto userDto;
}