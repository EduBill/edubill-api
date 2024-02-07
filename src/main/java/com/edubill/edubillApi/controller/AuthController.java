package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.dto.user.SignInRequestDto;
import com.edubill.edubillApi.dto.user.SignUpRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.verification.ExistUserRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.UserAlreadyExistsException;

import com.edubill.edubillApi.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;

@Slf4j
@RestController
@RequestMapping("/v1/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 인증번호 발송 API
    @PostMapping("/send-verification-number")
    public ResponseEntity<VerificationResponseDto> sendVerificationNumber(@RequestBody String phoneNumber) {
        VerificationResponseDto verificationResponseDto = authService.sendVerificationNumber(phoneNumber);
        return new ResponseEntity<>(verificationResponseDto, HttpStatus.OK);

    }

    // 인증번호 확인 API
    @PostMapping("/verifyNumber")
    public ResponseEntity<String> verifyNumber(@Valid @RequestBody VerificationRequestDto verificationRequestDto) {

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
    public ResponseEntity<Boolean> isExistsUser(@Valid @RequestBody ExistUserRequestDto existUserRequestDto) {

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
    public ResponseEntity<UserDto> signUp(@Valid @RequestBody SignUpRequestDto signUpRequestDto) {
        UserDto signUpUser = authService.signUp(signUpRequestDto);

        return new ResponseEntity<>(signUpUser, HttpStatus.OK);
    }

    // 로그인 API
    //@PostMapping("/signin")
    public ResponseEntity<UserDto> signIn(@Valid @RequestBody SignInRequestDto loginRequestDto) {
        String phoneNumber = loginRequestDto.getPhoneNumber();
        String requestId = loginRequestDto.getRequestId();

        UserDto signInUser = authService.signIn(phoneNumber, requestId);

        return new ResponseEntity<>(signInUser, HttpStatus.OK);
    }

}