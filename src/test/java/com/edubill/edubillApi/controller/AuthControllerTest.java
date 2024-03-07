package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.config.TestcontainerConfig;
import com.edubill.edubillApi.dto.user.ExistUserRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.jwt.JwtProvider;
import com.edubill.edubillApi.repository.RequestIdRepository;
import com.edubill.edubillApi.repository.RequestIdRepositoryMap;
import com.edubill.edubillApi.repository.UserRepository;
import com.edubill.edubillApi.repository.VerificationRepository;
import com.edubill.edubillApi.repository.VerificationRepositoryMap;
import com.edubill.edubillApi.service.AuthService;
import com.edubill.edubillApi.service.AuthServiceMock;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.shaded.org.checkerframework.checker.units.qual.A;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@TestcontainerConfig
@Transactional
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VerificationRepository verificationRepositoryMap;

    private AuthService authServiceMock;


    private AuthController authController;

    @BeforeEach
    void setup() {
        initMocks(this);
        VerificationRepository verificationRepositoryMap = new VerificationRepositoryMap();
        RequestIdRepository requestIdRepository = new RequestIdRepositoryMap();
        authServiceMock = new AuthServiceMock(userRepository, verificationRepositoryMap, requestIdRepository);

        authController = new AuthController(jwtProvider, authServiceMock);
        this.mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    @DisplayName("전화번호 유효성 검사")
    void testInvalidPhoneNumber() {
        //given
        String requestId = null;
        String phoneNumber = "01012345678234345535";
        ExistUserRequestDto requestDto = new ExistUserRequestDto(requestId, phoneNumber);

        //when
        Set<ConstraintViolation<ExistUserRequestDto>> violations = validator.validate(requestDto);
        List<String> messages = new ArrayList<>();

        for (ConstraintViolation<ExistUserRequestDto> violation : violations) {
            log.info("message = {}", violation.getMessage());
            messages.add(violation.getMessage());
        }

        //then
        assertThat(messages).contains("-을 제외한 11자리 번호를 입력해주세요", "고유요청은 필수입니다.");
    }


    @Test
    @DisplayName("인증번호 확인")
    void verificationTest() throws Exception {
        //given
        String phoneNumber = "01012345678";
        VerificationResponseDto verificationResponse = authServiceMock.sendVerificationNumber(phoneNumber);
        String requestId = verificationResponse.getRequestId();

        //when
        VerificationRequestDto verificationRequest = new VerificationRequestDto(requestId,"123456", "01012345678");

        //then
        mockMvc.perform(post("/v1/auth/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(verificationRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(requestId));
    }
}
