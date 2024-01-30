package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.JoinRequestDto;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.verification.ExistUserRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.exception.LoginFailedException;
import com.edubill.edubillApi.exception.UserAlreadyExistsException;
import com.edubill.edubillApi.repository.UserRepository;
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
    private final UserRepository userRepository;

    // 인증번호 발송 API
    @PostMapping("/send-verification-number")
    public ResponseEntity<VerificationResponseDto> sendVerificationNumber(@RequestBody String phoneNumber) {
        VerificationResponseDto verificationResponseDto = authService.sendVerificationNumber(phoneNumber);
        return new ResponseEntity<>(verificationResponseDto, HttpStatus.OK);
    }


    // 인증번호 확인 API
    // VerificationResponseDto를 재사용
    @PostMapping("/verifyNumber")
    public ResponseEntity<String> verifyNumber(@RequestBody VerificationRequestDto verificationRequestDto) {

        String verificationNumber = verificationRequestDto.getVerificationNumber();
        String requestId = verificationRequestDto.getRequestId();

        //try-catch 추가필요
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

    @PostMapping("/signin")
    public ResponseEntity<User> signIn(@Valid @RequestBody LoginRequestDto loginRequestDto) {
        // 로그인 API
        String phoneNumber = loginRequestDto.getPhoneNumber();
        String requestId = loginRequestDto.getRequestId();

        User loginUser = authService.signIn(phoneNumber, requestId);

        return new ResponseEntity<>(loginUser,HttpStatus.OK);
    }

    // 회원가입 API
    @PostMapping("/signup")
    public ResponseEntity<User> signUp(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        User joinedUser = authService.signUp(joinRequestDto);
        return new ResponseEntity<>(joinedUser,HttpStatus.OK);
    }
}
