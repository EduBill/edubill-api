package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.dto.user.ExistUserRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
@Transactional
public class AuthControllerTest {

    @Autowired
    private Validator validator;

    @Test
    @DisplayName("전화번호 유효성 검사")
    void testInvalidPhoneNumber() {
        //given
        String phoneNumber = "01012345678234345535";
        String requestId = null;
        ExistUserRequestDto requestDto = new ExistUserRequestDto(phoneNumber, requestId);

        //when
        Set<ConstraintViolation<ExistUserRequestDto>> violations = validator.validate(requestDto);
        List<String> messages = new ArrayList<>();

        for (ConstraintViolation<ExistUserRequestDto> violation : violations) {
            log.info("message = {}", violation.getMessage());
            messages.add(violation.getMessage());
        }

        //then
        assertThat(messages).contains("-을 제외한 10자리 번호를 입력해주세요", "고유요청은 필수입니다.");
    }
}
