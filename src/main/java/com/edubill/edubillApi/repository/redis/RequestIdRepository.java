package com.edubill.edubillApi.repository.redis;

public interface RequestIdRepository {
    void setRequestId(String key, String value);
    String getRequestId(String key);
}
