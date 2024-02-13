package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.user.ExistUserRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.UserAlreadyExistsException;

import com.edubill.edubillApi.jwt.JwtProvider;
import com.edubill.edubillApi.jwt.JwtToken;
import com.edubill.edubillApi.service.AuthServiceImpl;
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
@RequestMapping("/v1/auth/")
@RequiredArgsConstructor
public class AuthController {

    private final JwtProvider jwtProvider;
    private final AuthServiceImpl authServiceImpl;

    // 인증번호 발송 API
    @PostMapping("/send-verification-number")
    public ResponseEntity<VerificationResponseDto> sendVerificationNumber(@RequestBody String phoneNumber) {
        VerificationResponseDto verificationResponseDto = authServiceImpl.sendVerificationNumber(phoneNumber);
        return new ResponseEntity<>(verificationResponseDto, HttpStatus.OK);

    }

    // 인증번호 확인 API
    @PostMapping("/verifyNumber")
    public ResponseEntity<String> verifyNumber(@Validated @RequestBody VerificationRequestDto verificationRequestDto) {

        String verificationNumber = verificationRequestDto.getVerificationNumber();
        String requestId = verificationRequestDto.getRequestId();

        try {
            authServiceImpl.verifyNumber(verificationNumber, requestId);
        } catch (NoSuchElementException e) {
            log.error("인증번호 존재하지 않음={}", e.getMessage());
            return new ResponseEntity<>(requestId, HttpStatus.NOT_FOUND);// 에러코드 수정 필요
        } catch (IllegalArgumentException e) {
            log.error("인증번호 불일치={}", e.getMessage());
            return new ResponseEntity<>(requestId, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(requestId, HttpStatus.OK);
    }

    // 사용자 유무 확인 API
    @PostMapping("/exists-user")
    public ResponseEntity<Boolean> isExistsUser(@Validated @RequestBody ExistUserRequestDto existUserRequestDto) {

        String phoneNumber = existUserRequestDto.getPhoneNumber();
        String requestId = existUserRequestDto.getRequestId();
        log.info("request_id = {}", requestId);

        if (authServiceImpl.isExistsUser(phoneNumber)) {
            throw new UserAlreadyExistsException("이미 존재하는 회원.");
        }
        return new ResponseEntity<>(authServiceImpl.isExistsUser(phoneNumber), HttpStatus.OK);
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@Validated @RequestBody SignupRequestDto signupRequestDto) {
        UserDto signupUser = authServiceImpl.signUp(signupRequestDto);

        return new ResponseEntity<>(signupUser, HttpStatus.OK);
    }

    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<JwtToken> login(@Validated @RequestBody LoginRequestDto loginRequestDto, HttpServletResponse response) {

        UserDto loginUser = authServiceImpl.login(loginRequestDto);
        UserRole userRole = UserRole.ACADEMY; // 수정필요

        JwtToken token = jwtProvider.createTokenByLogin(loginUser.getPhoneNumber(), userRole);

        response.addHeader(JwtProvider.AUTHORIZATION_HEADER, token.getAccessToken());// 헤더에 access token 만 싣기

        return new ResponseEntity<>(token, HttpStatus.OK);
    }

}