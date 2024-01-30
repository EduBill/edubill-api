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

        String enteredNumber = verificationRequestDto.getEnteredNumber();
        String requestId = verificationRequestDto.getRequestId();

        //try-catch 추가필요
        try {
            String message = authService.verifyNumber(enteredNumber, requestId);
            log.info("message={}", message);
        } catch (NoSuchElementException e) {
            log.info("요청에 해당하는 정보가 없습니다. ={}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.info("인증번호가 일치하지 않습니다. ={}", e.getMessage());
        }
        return new ResponseEntity<>(requestId, HttpStatus.OK);
    }

    // 사용자 유무 확인 API
    @PostMapping("/exists-user")
    public ResponseEntity<Boolean> isExistsUser(@RequestBody ExistUserRequestDto existUserRequestDto) {

        String phoneNumber = existUserRequestDto.getPhoneNumber();
        String requestId = existUserRequestDto.getRequestId();
        log.info("request_id = {}", requestId);

        if (authService.isExistsUser(phoneNumber)) {
            throw new UserAlreadyExistsException("이미 존재하는 회원.");
        }
        return new ResponseEntity<>(authService.isExistsUser(phoneNumber), HttpStatus.OK);
    }

    @PostMapping("/login")
    public ResponseEntity<User> login(@RequestBody LoginRequestDto loginRequestDto) {
        // 로그인 API
        String phoneNumber = loginRequestDto.getPhoneNumber();
        String requestId = loginRequestDto.getRequestId();

        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new LoginFailedException("해당 전화번호를 가진 유저가 없음."));


        User loginUser = authService.login(phoneNumber, requestId);

        return new ResponseEntity<>(loginUser,HttpStatus.OK);
    }

    // 회원가입 API
    @PostMapping("/join")
    public ResponseEntity<User> join(@RequestBody JoinRequestDto joinRequestDto) {
        User joinedUser = authService.join(joinRequestDto);
        return new ResponseEntity<>(joinedUser,HttpStatus.OK);
    }


}
