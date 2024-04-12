package com.edubill.edubillApi.auth.repository;

public interface RequestIdRepository {
    void setRequestId(String key, String value);
    String getRequestId(String key);
}
