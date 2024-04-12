package com.edubill.edubillApi.auth.repository;

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
