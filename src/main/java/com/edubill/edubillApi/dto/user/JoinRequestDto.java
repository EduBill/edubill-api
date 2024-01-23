package com.edubill.edubillApi.dto.user;

import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {

    private String userName;
    private String userEmail;
    private String userPassword;
}
