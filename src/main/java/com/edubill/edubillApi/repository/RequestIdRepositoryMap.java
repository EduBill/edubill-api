package com.edubill.edubillApi.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

//@Repository
public class RequestIdRepositoryMap implements RequestIdRepository{

    private final HashMap<String, String> requestIdMap = new HashMap<>();

    @Override
    public void setRequestId(String phoneNumber, String requestId) {
        requestIdMap.put(phoneNumber, requestId);
    }
    @Override
    public String getRequestId(String phoneNumber) {
        return requestIdMap.get(phoneNumber);
    }
}
