package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.jwt.JwtProvider;
import com.edubill.edubillApi.repository.UserRepository;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static com.edubill.edubillApi.domain.UserRole.ADMIN;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Slf4j
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtProvider jwtProvider;
    @Autowired
    private UserRepository userRepository;


    @Test
    @Commit
    @DisplayName("회원가입 테스트")
    void signupTest() throws Exception {
        // given
        SignupRequestDto signupRequestDto = new SignupRequestDto("jaehak", "01087329001", "dfsdfsdf");

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(signupRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userName").value("jaehak"))
                .andExpect(jsonPath("$.phoneNumber").value("01087329001"))
                .andExpect(jsonPath("$.userRole").value("ADMIN")); // 수정 필요

        // then
        User savedUser = userRepository.findByPhoneNumber("01087329001").orElse(null);
        assertEquals("jaehak", savedUser.getUserName());
        assertEquals(ADMIN, savedUser.getUserRole());
    }

    @Test
    @DisplayName("로그인 시 토큰 발급 및 auth 테스트")
    void generateTokenAuthTest() throws Exception {

        //== generateToken==//
        // given
        LoginRequestDto loginRequestDto = new LoginRequestDto("01087329001", "dfsdfsdf");

        // when
        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.post("/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDto)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andExpect(jsonPath("$.accessToken").isNotEmpty());
        String bearerToken = result.andReturn().getResponse().getHeader(HttpHeaders.AUTHORIZATION);
        String accessToken = bearerToken.substring(7);

        // then
        assertTrue(jwtProvider.validateToken(accessToken));
        Claims userInfo = jwtProvider.getUserInfoFromToken(accessToken);
        assertEquals("01087329001", userInfo.getSubject());


        //==auth check==//
        mockMvc.perform(MockMvcRequestBuilders.get("/health")
                        .header(HttpHeaders.AUTHORIZATION, bearerToken))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello world"));
    }

}