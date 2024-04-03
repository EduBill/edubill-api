package com.edubill.edubillApi.service;

import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.user.ExistUserRequestDto;
import com.edubill.edubillApi.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@SpringBootTest
@Slf4j
@TestcontainerConfig
class AuthServiceTest {

    @Autowired
    private @Qualifier("authServiceMock") AuthService authService;
    @Autowired
    private UserRepository userRepository;


    @Test
    @DisplayName("회원 가입")
    void signUpTest() {

        //given
        String requestId = UUID.randomUUID().toString();
        SignupRequestDto requestA = new SignupRequestDto(requestId, "userA", "01012345678");

        //when
        UserDto userA = authService.signUp(requestA);

        //then
         User findUser = userRepository.findByPhoneNumber("01012345678").orElse(null);
        log.info("findUser={}", findUser);
        assertThat(findUser.getUserId()).isEqualTo(userA.getUserId());
    }

    @Test
    @DisplayName("중복 회원가입 실패")
    void signUpDuplicateTest() {
        //given
        String requestId = UUID.randomUUID().toString();
        SignupRequestDto signUpRequest = new SignupRequestDto(requestId,"userB", "01011111111");
        UserDto userB = authService.signUp(signUpRequest);

        // 새로운 회원 가입 전에 /exists/user 엔드포인트를 무조건 거쳐야함
        //when
        ExistUserRequestDto existRequest = new ExistUserRequestDto(requestId, "01011111111");

        //then
//        assertThatThrownBy(() -> authService.isExistsUser(existRequest.getPhoneNumber()))
//                .isInstanceOf(UserAlreadyExistsException.class);
        assertThat(authService.isExistsUser(existRequest.getPhoneNumber())).isTrue();
    }

}