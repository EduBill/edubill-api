package com.edubill.edubillApi.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DepartmentType {
    LIBERAL_ARTS("문과"),
    NATURAL_SCIENCES("이과"),
    PHYSICAL_EDUCATION("예체능"),
    ETC("기타/해당사항 없음");

    private final String description;

    DepartmentType(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static DepartmentType fromDescription(String description) {
        for (DepartmentType departmentType : DepartmentType.values()) {
            if (departmentType.description.equals(description)) {
                return departmentType;
            }
        }
        throw new IllegalArgumentException("Unknown departmentType description: " + description);
    }
}
