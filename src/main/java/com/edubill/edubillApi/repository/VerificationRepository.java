package com.edubill.edubillApi.repository;

public interface VerificationRepository {
    void put(String key, String value);
    String get(String key);
}