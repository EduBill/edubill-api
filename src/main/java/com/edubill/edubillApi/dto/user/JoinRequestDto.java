package com.edubill.edubillApi.dto.user;

import com.edubill.edubillApi.domain.UserRole;
import lombok.*;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class JoinRequestDto {

    private String userName;
    private String phoneNumber;
    private String requestId;
    private UserRole userRole;
}
