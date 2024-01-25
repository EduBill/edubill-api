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
//@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    //@RequestBody 객체 변환
    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/join")
    public UserDto join(@Valid @RequestBody JoinRequestDto joinRequestDto) {
        return authService.join(joinRequestDto);
    }

    @PostMapping("/login")
    public ResponseEntity login(@Valid @RequestBody LoginRequestDto loginRequestDto) {

        User loginUser = authService.login(loginRequestDto);

        return null;
    }
}
