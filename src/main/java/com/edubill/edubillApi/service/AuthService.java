package com.edubill.edubillApi.service;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.UserDto;
import com.edubill.edubillApi.dto.user.JoinRequestDto;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.exception.LoginFailedException;
import com.edubill.edubillApi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public UserDto join(JoinRequestDto joinRequestDto) {
        return null;
    }

    public User login(LoginRequestDto loginRequestDto) {
        User user = userRepository.findByUserEmail(loginRequestDto.getUserEmail())
                .orElseThrow(() -> new LoginFailedException("해당 이메일을 가진 사용자가 없습니다."));

    }
}
