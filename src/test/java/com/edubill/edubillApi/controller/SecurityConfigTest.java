package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.jwt.JwtProvider;
import com.edubill.edubillApi.repository.UserRepository;

import com.edubill.edubillApi.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.edubill.edubillApi.domain.UserRole.ACADEMY;
import static com.edubill.edubillApi.domain.UserRole.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestcontainerConfig
@Transactional
@Slf4j
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private @Qualifier("authServiceMock") AuthService authService;


    @Test
    @DisplayName("회원가입 후 로그인 진행")
    void signupAndLoginTest() throws Exception {
        //== 인증번호 발급 ==//
        String phoneNumber = "01012345678";
        VerificationResponseDto verificationResponse = authService.sendVerificationNumber(phoneNumber);
        String requestId = verificationResponse.getRequestId();

        //== 인증번호 검증 ==//
        VerificationRequestDto verificationRequest = new VerificationRequestDto(requestId, "123456", "01012345678");
        mockMvc.perform(post("/v1/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(requestId));

        //== 회원가입 ==//
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto(requestId, "edubill", "01012345678");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("edubill"))
                .andExpect(jsonPath("$.phoneNumber").value("01012345678"))
                .andExpect(jsonPath("$.userRole").value("ACADEMY")); // 수정 필요

        // then
        User savedUser = userRepository.findByPhoneNumber("01012345678").orElse(null);
        assertEquals("edubill", savedUser.getUserName());
        assertEquals(ACADEMY, savedUser.getUserRole());


        //== 로그인 및 토큰 생성 ==//
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto(requestId, "01012345678");

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(jsonPath("$.jwtToken.accessToken").isNotEmpty());
        String bearerToken = result.andReturn().getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = bearerToken.substring(7);

        // then
        assertTrue(jwtProvider.validateToken(accessToken));
        Claims userInfo = jwtProvider.getUserInfoFromToken(accessToken);
        assertEquals("01012345678", userInfo.getSubject());


        //== 토큰 인증 ==//
        mockMvc.perform(MockMvcRequestBuilders.get("/health")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello world"));
    }
}