package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.user.ExistUserRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;

import com.edubill.edubillApi.jwt.JwtProvider;
import com.edubill.edubillApi.jwt.JwtToken;
import com.edubill.edubillApi.service.AuthService;

import jakarta.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    public AuthController(JwtProvider jwtProvider, @Qualifier("authServiceImpl")AuthService authService) {
        this.jwtProvider = jwtProvider;
        this.authService = authService;
    }

    // 인증번호 발송 API
    @PostMapping("/send/verification/number")
    public ResponseEntity<VerificationResponseDto> sendVerificationNumber(@RequestBody String phoneNumber) {
        VerificationResponseDto verificationResponseDto = authService.sendVerificationNumber(phoneNumber);

        log.info("requestId = {}, 인증번호 ={}", verificationResponseDto.getRequestId(),verificationResponseDto.getVerificationNumber());
        return ResponseEntity.ok(verificationResponseDto);
    }

    // 인증번호 확인 API
    @PostMapping("/verify/number")
    public ResponseEntity<String> verifyNumber(@Validated @RequestBody VerificationRequestDto verificationRequestDto) {

        String requestId = verificationRequestDto.getRequestId();
        String verificationNumber = verificationRequestDto.getVerificationNumber();

        Boolean validNumber = authService.verifyNumber(requestId, verificationNumber);
        if (validNumber) {
            return ResponseEntity.ok(requestId);
        }
        return new ResponseEntity<>(requestId, HttpStatus.BAD_REQUEST);
    }

    // 사용자 유무 확인 API
    @PostMapping("/exists/user")
    public ResponseEntity<Boolean> isExistsUser(@Validated @RequestBody ExistUserRequestDto existUserRequestDto) {

        String phoneNumber = existUserRequestDto.getPhoneNumber();
        String requestId = existUserRequestDto.getRequestId();
        log.info("request_id = {}", requestId);

        if (authService.isExistsUser(phoneNumber)) {
            return ResponseEntity.ok(true);
        }
        // 기존회원이 없기때문에 새로 회원을 가입시킬 수 있음.
        return ResponseEntity.ok(false);
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        UserDto signupUser = authService.signUp(signupRequestDto);
        return ResponseEntity.ok(signupUser);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UserDto userDto = authService.login(loginRequestDto);
        JwtToken token = jwtProvider.createTokenByLogin(userDto.getPhoneNumber(), userDto.getUserRole());
        response.addHeader(JwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());// 헤더에 access token 만 싣기

        return ResponseEntity.ok(token);
    }
}