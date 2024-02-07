package com.edubill.edubillApi.controller;

import com.edubill.edubillApi.dto.verification.ExistUserRequestDto;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest
public class AuthControllerTest {

    @Autowired
    private Validator validatorInjected;

    @Test
    @DisplayName("전화번호 유효성 검사")
    void testInvalidPhoneNumber() {
        //given
        String phoneNumber = "01012345678234345535";
        String requestId = null;
        ExistUserRequestDto requestDto = new ExistUserRequestDto(phoneNumber, requestId);

        //when
        Set<ConstraintViolation<ExistUserRequestDto>> validate = validatorInjected.validate(requestDto);


        //then
        Iterator<ConstraintViolation<ExistUserRequestDto>> iterator = validate.iterator();
        List<String> messages = new ArrayList<>();

        while (iterator.hasNext()) {
            ConstraintViolation<ExistUserRequestDto> next = iterator.next();
            messages.add(next.getMessage());
            log.info("message = {}", next.getMessage());

        }

        assertThat(messages).contains("휴대폰 번호의 양식과 맞지 않습니다.", "고유요청은 필수입니다.");

    }

}
