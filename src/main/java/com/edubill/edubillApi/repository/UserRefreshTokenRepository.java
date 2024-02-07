package com.edubill.edubillApi.repository;


import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class UserRefreshTokenRepository {

    private final Map<String, String> refreshTokenMap = new HashMap<>();

    //== refreshToken==//
    public void setRefreshToken(String key, String refreshToken, long refreshTokenTime) {
        refreshTokenMap.put(key, refreshToken);
    }

    public String getRefreshToken(String key) {
        return refreshTokenMap.get(key);
    }

    public void deleteRefreshToken(String key) {
        refreshTokenMap.remove(key);
    }

    public boolean hasKey(String key) {
        return refreshTokenMap.containsKey(key);
    }


    //== accessToken ==//
    public void setBlackList(String accessToken, String message, Long minutes) {
        refreshTokenMap.put(accessToken, message);
    }
    public String getBlackList(String key) {
        return refreshTokenMap.get(key);
    }
    public boolean deleteBlackList(String key) {
        return refreshTokenMap.remove(key) != null;
    }

    public void clear() {
        refreshTokenMap.clear();
    }
}
