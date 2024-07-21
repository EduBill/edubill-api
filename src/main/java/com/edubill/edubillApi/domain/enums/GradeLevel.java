package com.edubill.edubillApi.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
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

    //객체를 Jjso으로 직렬화
    @JsonValue
    public String getDescription() {
        return description;
    }

    //JSON에서 객체로 역직렬화
    @JsonCreator
    public static GradeLevel fromDescription(String description) {
        for (GradeLevel gradeLevel : GradeLevel.values()) {
            if (gradeLevel.description.equals(description)) {
                return gradeLevel;
            }
        }
        throw new IllegalArgumentException("Unknown gradeLevel description : " + description);
    }
}
