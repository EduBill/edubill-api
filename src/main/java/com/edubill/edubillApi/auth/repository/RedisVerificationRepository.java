package com.edubill.edubillApi.auth.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisVerificationRepository implements VerificationRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisVerificationRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setVerificationNumber(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getVerificationNumber(String key) {
        return redisTemplate.opsForValue().get(key);

    }
}
