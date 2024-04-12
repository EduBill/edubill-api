package com.edubill.edubillApi.user.response;

import com.edubill.edubillApi.auth.jwt.JwtToken;
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