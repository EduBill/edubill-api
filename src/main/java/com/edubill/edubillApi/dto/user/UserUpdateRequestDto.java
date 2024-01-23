package com.edubill.edubillApi.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto implements Serializable {

    private Long userId;
    private String userName;
    private Long phoneNumber;
}
