package com.edubill.edubillApi.auth.service;

import com.edubill.edubillApi.user.request.LoginRequestDto;
import com.edubill.edubillApi.user.request.SignupRequestDto;
import com.edubill.edubillApi.user.response.UserDto;
import com.edubill.edubillApi.user.response.VerificationResponseDto;


public interface AuthService {
    VerificationResponseDto sendVerificationNumber(String phoneNumber);
    Boolean verifyNumber(String requestId, String inputVerificationNumber);
    UserDto signUp(SignupRequestDto signupRequestDto);
    UserDto login(LoginRequestDto loginRequestDto);
    Boolean isExistsUser(String phoneNumber);
    void requestIdForPhoneNumber(String phoneNumber, String requestId);
    Boolean isRequestIdValidForPhoneNumber(String phoneNumber, String clientRequestId);
}