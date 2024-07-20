package com.edubill.edubillApi.domain.enums;

import lombok.Getter;

@Getter
public enum SchoolType {
    ELEMENTARY("초등학교"),
    MIDDLE("중학교"),
    HIGH("고등학교"),
    ETC("기타/해당사항 없음");

    private final String description;

    SchoolType(String description) {
        this.description = description;
    }
}
