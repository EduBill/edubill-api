package com.edubill.edubillApi.repository.verification;

public interface VerificationRepository {
    void setVerificationNumber(String key, String value);
    String getVerificationNumber(String key);
}