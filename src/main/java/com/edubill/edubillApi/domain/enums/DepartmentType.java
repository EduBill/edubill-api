package com.edubill.edubillApi.domain.enums;

import lombok.Getter;

@Getter
public enum DepartmentType {
    LIBERAL_ARTS("문과"),
    NATURAL_SCIENCES("이과"),
    PHYSICAL_EDUCATION("예체능");

    private final String description;

    DepartmentType(String description) {
        this.description = description;
    }
}
