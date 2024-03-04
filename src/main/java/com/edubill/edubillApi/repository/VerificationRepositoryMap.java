package com.edubill.edubillApi.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class VerificationRepositoryMap implements VerificationRepository{
    private final HashMap<String, String> verificationMap = new HashMap<>();

    @Override
    public void setVerificationNumber(String requestId, String verificationNumber) {
        verificationMap.put(requestId, verificationNumber);
    }
    @Override
    public String getVerificationNumber(String requestId) {
        return verificationMap.get(requestId);
    }

}