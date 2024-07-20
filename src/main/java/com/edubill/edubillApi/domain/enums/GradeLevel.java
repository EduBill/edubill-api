package com.edubill.edubillApi.domain.enums;

import lombok.Getter;

@Getter
public enum GradeLevel {
    FIRST("1학년"),
    SECOND("2학년"),
    THIRD("3학년"),
    FOURTH("4학년"),
    FIFTH("5학년"),
    SIXTH("6학년"),
    ETC("기타/해당사항 없음");

    private final String description;

    GradeLevel(String description) {
        this.description = description;
    }
}
