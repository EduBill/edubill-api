package com.edubill.edubillApi.dto.user;

import com.edubill.edubillApi.domain.AuthInfo;
import com.edubill.edubillApi.domain.User;
import com.edubill.edubillApi.domain.UserRole;
import com.edubill.edubillApi.jwt.JwtToken;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.io.Serializable;

@Schema(description = "사용자 dto")
@Getter
@AllArgsConstructor
public class UserDto implements Serializable {

    @Schema(description = "userId", type = "Long", example = "1")
    private Long userId;

    @Schema(description = "userName", type = "String", example = "홍길동")
    private String userName;

    @Schema(description = "phoneNumber", type = "String", example = "01012345678")
    private String phoneNumber;

    @Schema(description = "userRole", type = "UserRole", example = "ACADEMY")
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