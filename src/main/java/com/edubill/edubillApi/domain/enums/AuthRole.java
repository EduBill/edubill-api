package com.edubill.edubillApi.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuthRole {

    USER("ROLE_USER", "사용자 권한"),
    ADMIN("ROLE_ADMIN", "관리자 권한");

    private final String code;
    private final String displayName;

}
