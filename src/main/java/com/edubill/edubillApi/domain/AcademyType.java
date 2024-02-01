package com.edubill.edubillApi.domain;

import lombok.Getter;

@Getter
public enum AcademyType {

    MATH("수학 학원"),
    ENGLISH("영어 학원"),
    SCIENCE("과학 학원"),
    ART("미술 학원");

    private final String typeDescription;

    AcademyType(String typeDescription) {
        this.typeDescription = typeDescription;
    }

}
