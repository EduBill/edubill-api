package com.edubill.edubillApi.jwt;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
@Builder
public class JwtToken {

    private final String grantType;
    private final String accessToken;
    private final String refreshToken;

}
