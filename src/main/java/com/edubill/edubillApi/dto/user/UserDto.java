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
    private UserRole userRole;


    // of 메서드를 통해 유저 객체를 DTO에 담아 반환
    public static UserDto of(User user) {
        return new UserDto(
                user.getUserId(),
                user.getUserName(),
                user.getPhoneNumber(),
                user.getUserRole()
        );
    }
}