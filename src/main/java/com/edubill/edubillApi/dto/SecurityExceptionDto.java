package com.edubill.edubillApi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SecurityExceptionDto {

    private int statusCode;
    private String message;

}