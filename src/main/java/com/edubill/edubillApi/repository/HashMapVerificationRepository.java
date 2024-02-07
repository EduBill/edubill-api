package com.edubill.edubillApi.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;

@Repository
public class HashMapVerificationRepository implements VerificationRepository{
    private final HashMap<String, String> verificationMap = new HashMap<>();

    @Override
    public void setVerificationNumber(String key, String value) {
        verificationMap.put(key, value);
    }
    @Override
    public String getVerificationNumber(String key) {
        return verificationMap.get(key);
    }

}