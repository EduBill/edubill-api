package com.edubill.edubillApi.domain;

public enum UserType {
    ACADEMY("학원"),
    STUDENT("학생"),
    PARENT("학부모");

    private final String description;

    UserType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
