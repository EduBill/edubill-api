package com.edubill.edubillApi.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static SchoolType fromDescription(String description) {
        for (SchoolType schoolType : SchoolType.values()) {
            if (schoolType.description.equals(description)) {
                return schoolType;
            }
        }
        throw new IllegalArgumentException("Unknown schoolType description: " + description);
    }
}
