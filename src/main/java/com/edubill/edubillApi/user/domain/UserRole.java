package com.edubill.edubillApi.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum UserRole {

    PARENTS("ROLE_PARENTS", "학부모 권한"),
    STUDENT("ROLE_STUDENT", "학생 권한"),
    ACADEMY("ROLE_ACADEMY", "학원 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한");

    private final String code;
    private final String displayName;

    public static UserRole of(String code) {
        return Arrays.stream(UserRole.values())
                .filter(r -> r.getCode().equals(code))
                .findAny()
                .orElse(ADMIN);
    }
}
