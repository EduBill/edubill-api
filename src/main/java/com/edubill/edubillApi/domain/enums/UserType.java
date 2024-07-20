package com.edubill.edubillApi.domain.enums;

import lombok.Getter;

@Getter
public enum UserType {
    ACADEMY("학원"),
    STUDENT("학생"),
    PARENT("학부모");

    private final String description;

    UserType(String description) {
        this.description = description;
    }
}
