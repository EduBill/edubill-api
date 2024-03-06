package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.dto.user.ExistUserRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationRequestDto;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;
import com.edubill.edubillApi.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private @Qualifier("authServiceMock") AuthService authService;

    @Autowired
    private Validator validator;


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
        VerificationResponseDto verificationResponse = authService.sendVerificationNumber(phoneNumber);
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
