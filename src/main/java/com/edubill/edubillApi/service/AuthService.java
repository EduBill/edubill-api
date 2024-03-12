package com.edubill.edubillApi.service;

import com.edubill.edubillApi.dto.user.*;
import com.edubill.edubillApi.dto.verification.VerificationResponseDto;


public interface AuthService {
    VerificationResponseDto sendVerificationNumber(String phoneNumber);
    Boolean verifyNumber(String requestId, String inputVerificationNumber);
    UserDto signUp(SignupRequestDto signupRequestDto);
    UserDto login(LoginRequestDto loginRequestDto);
    ExistUserResponseDto isExistsUser(String phoneNumber);
    RequestIdResponseDto requestIdForPhoneNumber(String phoneNumber, String requestId);
    Boolean isRequestIdValidForPhoneNumber(String phoneNumber, String clientRequestId);
}