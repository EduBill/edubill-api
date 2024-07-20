package com.edubill.edubillApi.domain.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum DayOfWeek{
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일");

    private final String description;

    DayOfWeek(String description) {
        this.description = description;
    }

    @JsonValue
    public String getDescription() {
        return description;
    }

    @JsonCreator
    public static DayOfWeek fromDescription(String description) {
        for (DayOfWeek dayOfWeek : DayOfWeek.values()) {
            if (dayOfWeek.description.equals(description)) {
                return dayOfWeek;
            }
        }
        throw new IllegalArgumentException("Unknown DayOfWeek description: " + description);
    }
}
