package com.edubill.edubillApi.repository.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRequestIdRepository implements RequestIdRepository {

    private final RedisTemplate<String, String> redisTemplate;

    public RedisRequestIdRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void setRequestId(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public String getRequestId(String key) {
        return redisTemplate.opsForValue().get(key);
    }
}
