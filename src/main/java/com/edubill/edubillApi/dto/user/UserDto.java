package com.edubill.edubillApi.dto.user;

import com.edubill.edubillApi.domain.AuthInfo;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.domain.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Getter
@AllArgsConstructor
public class UserDto implements Serializable {

    private Long userId;
    private String userName;
    private String phoneNumber;


    public static UserDto toDto(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUserName(),
                user.getPhoneNumber()
        );
    }
}