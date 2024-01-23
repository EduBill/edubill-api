package com.edubill.edubillApi.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserDto {

    private String userName;
    private String userEmail;
    private Long phoneNumber;
}
