package com.edubill.edubillApi.repository;

public interface RequestIdRepository {
    void setRequestId(String key, String value);
    String getRequestId(String key);
}
