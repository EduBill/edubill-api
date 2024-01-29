package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.dto.user.JoinRequestDto;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
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
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // 인증번호 발송 API
    @PostMapping("/send-verification-code")
    public ResponseEntity<VerificationResponseDto> sendVerificationCode(@RequestBody String phoneNumber) {

        log.info("phoneNumber={}", phoneNumber);
        VerificationResponseDto verificationResponseDto = authService.sendVerificationCode(phoneNumber);

        return new ResponseEntity<>(verificationResponseDto, HttpStatus.OK);
    }

    // 인증번호 확인 API
    // VerificationResponseDto를 재사용
    @PostMapping("/verify")
    public ResponseEntity<String> verify(@RequestBody VerificationResponseDto VerifyCodeRequestDto) {

        String enteredCode = VerifyCodeRequestDto.getVerificationCode();
        String requestId = VerifyCodeRequestDto.getRequestId();
        log.info("enteredCode={}", enteredCode);
        log.info("requestId={}", requestId);


        //try-catch 추가필요
        try {
            authService.verifyCode(enteredCode, requestId);
        } catch (NoSuchElementException e) {
            log.info("요청에 해당하는 정보가 없습니다. ={}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("코드가 일치하지 않습니다. ={}", e.getMessage());
        }
        return new ResponseEntity<>(requestId, HttpStatus.OK);
    }

    // 유저 유무 확인 API
    @PostMapping("/check-user")
    public ResponseEntity<CheckUserResponse> checkUser(@RequestBody CheckUserRequest request) {

        CheckUserResponse response = authService.checkUser(request.getPhoneNumber(), request.getRequestId());
        return ResponseEntity.ok(response);
    }


    // 로그인 API
    @PostMapping("/login")
    public ResponseEntity<LoginRequestDto> login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        LoginResponse response = authService.login(loginRequestDto.getPhoneNumber(), loginRequestDto.getRequestId());

        return ResponseEntity.ok(response);
    }

    // 회원가입 API
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/join")
    public UserDto join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        return authService.join(joinRequestDto);
    }
}
