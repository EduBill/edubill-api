package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.verification.ExistUserRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.UserAlreadyExistsException;

import com.edubill.edubillApi.jwt.JwtProvider;
import com.edubill.edubillApi.jwt.JwtToken;
import com.edubill.edubillApi.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/v1/auth/api")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthService authService;

    // 인증번호 발송 API
    @PostMapping("/send-verification-number")
    public ResponseEntity<VerificationResponseDto> sendVerificationNumber(@RequestBody String phoneNumber) {
        VerificationResponseDto verificationResponseDto = authService.sendVerificationNumber(phoneNumber);
        return new ResponseEntity<>(verificationResponseDto, HttpStatus.OK);

    }

    // 인증번호 확인 API
    @PostMapping("/verifyNumber")
    public ResponseEntity<String> verifyNumber(@Validated @RequestBody VerificationRequestDto verificationRequestDto) {

        String verificationNumber = verificationRequestDto.getVerificationNumber();
        String requestId = verificationRequestDto.getRequestId();

        try {
            String message = authService.verifyNumber(verificationNumber, requestId);
            log.info("message={}", message);
        } catch (NoSuchElementException e) {
            log.error("요청에 해당하는 정보가 없습니다. ={}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("인증번호가 일치하지 않습니다. ={}", e.getMessage());
        }
        return new ResponseEntity<>(requestId, HttpStatus.OK);
    }

    // 사용자 유무 확인 API
    @PostMapping("/exists-user")
    public ResponseEntity<Boolean> isExistsUser(@Validated @RequestBody ExistUserRequestDto existUserRequestDto) {

        String phoneNumber = existUserRequestDto.getPhoneNumber();
        String requestId = existUserRequestDto.getRequestId();
        log.info("request_id = {}", requestId);

        if (authService.isExistsUser(phoneNumber)) {
            throw new UserAlreadyExistsException("이미 존재하는 회원.");
        }
        return new ResponseEntity<>(authService.isExistsUser(phoneNumber), HttpStatus.OK);
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        UserDto signupUser = authService.signUp(signupRequestDto);

        return new ResponseEntity<>(signupUser, HttpStatus.OK);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UserDto loginUser = authService.login(loginRequestDto);
        UserRole userRole = UserRole.ADMIN; // 수정필요

        JwtToken token = jwtProvider.createTokenByLogin(loginUser.getPhoneNumber(), userRole);

        response.addHeader(JwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());// 헤더에 access token 만 싣기

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}