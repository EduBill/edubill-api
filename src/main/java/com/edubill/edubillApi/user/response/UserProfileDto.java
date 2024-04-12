package com.edubill.edubillApi.user.response;

import com.edubill.edubillApi.user.domain.User;
import io.swagger.v3.oas.annotations.media.Schema;

public record UserProfileDto(

        @Schema(description = "유저 이름")
        String username,

        @Schema(description = "유저 유형 - [학원, 학부모, 학생] 중 하나")
        String userType
) {
    public static UserProfileDto of(User user) {
        //TODO mock data를 위한 하드코딩 제거
        String userType = "학원";
        if (user.getUserType() != null) {
            userType = user.getUserType().getDescription();
        }
        return new UserProfileDto(user.getUserName(), userType);
    }
}
