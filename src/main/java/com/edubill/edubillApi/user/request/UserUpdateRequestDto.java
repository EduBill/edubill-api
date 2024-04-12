package com.edubill.edubillApi.user.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserUpdateRequestDto implements Serializable {

    private String userName;
    private String phoneNumber;
}
