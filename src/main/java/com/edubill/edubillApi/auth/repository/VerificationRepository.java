package com.edubill.edubillApi.auth.repository;

public interface VerificationRepository {
    void setVerificationNumber(String key, String value);
    String getVerificationNumber(String key);
}