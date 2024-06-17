package com.edubill.edubillApi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Validator {

    @Value("${validation.depositorNameRegex}")
    private String depositorNameRegex;

    public boolean isValidDepositorName(String name) {
        return name != null && !name.matches(depositorNameRegex); // 영어가 포함되지 않는 경우
    }
}
