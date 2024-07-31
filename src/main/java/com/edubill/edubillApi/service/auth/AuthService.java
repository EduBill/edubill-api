package com.edubill.edubillApi.service.auth;

import com.edubill.edubillApi.dto.user.LoginRequestDto;
import com.edubill.edubillApi.dto.user.SignupRequestDto;
import com.edubill.edubillApi.dto.user.UserDto;
import com.edubill.edubillApi.dto.auth.VerificationResponseDto;


public interface AuthService {
    VerificationResponseDto sendVerificationNumber(String phoneNumber);
    Boolean verifyNumber(String requestId, String inputVerificationNumber);
    UserDto signUp(SignupRequestDto signupRequestDto);
    UserDto login(LoginRequestDto loginRequestDto);
    Boolean isExistsUser(String phoneNumber);
    void requestIdForPhoneNumber(String phoneNumber, String requestId);
    Boolean isRequestIdValidForPhoneNumber(String phoneNumber, String clientRequestId);
}