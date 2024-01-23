package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.UserDto;
import com.edubill.edubillApi.dto.user.JoinRequestDto;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final BCryptPasswordEncoder pwEncoder;
    private final AuthService authService;

    //@RequestBody 객체 변환
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/join")
    public UserDto join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        return authService.join(joinRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody LoginRequestDto loginRequestDto) {

        // 여기에 적절한 로직을 추가하여 응답을 생성가능
        // SON 형태의 응답을 생성
        User loginUser = authService.login(loginRequestDto);


        return ResponseEntity.ok(JwtTokenDto.builder()
                .userId(user.getUserId())
                .userEmail(user.getUserEmail())
                .nickname(user.getUserNickname())
                .accessToken(accessToken.getToken())
                .refreshToken(refreshToken.getToken())
                .build()
        );
    }

/*
    @GetMapping("/response-body-json-v1")
    public ResponseEntity<HelloData> responseBodyJsonV1() {
        HelloData helloData = new HelloData();
        helloData.setUsername("userA");
        helloData.setAge(20);
        return new ResponseEntity<>(helloData, HttpStatus.OK);
    }
*/


}
