package com.edubill.edubillApi.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Schema(description = "토큰 dto")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@RequiredArgsConstructor
public class JwtToken {

    @Schema(description = "accessToken", type = "String", example = "dsfsdfsdfsdfsdfsdfsdfsdfsdf")
    private final String accessToken;
}