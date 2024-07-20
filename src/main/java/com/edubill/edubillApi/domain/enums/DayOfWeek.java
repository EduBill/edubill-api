package com.edubill.edubillApi.domain.enums;

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
}
