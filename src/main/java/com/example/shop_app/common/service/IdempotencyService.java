package com.example.shop_app.common.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IdempotencyService {
    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String IN_PROGRESS = "IN_PROGRESS";
    private static final Duration LOCK_TIMEOUT = Duration.ofMinutes(2);
    private static final Duration COMPLETED_TIMEOUT = Duration.ofHours(24);

    public boolean lock(String key){
        String redisKey = "idempotency:" + key;
        Boolean acquired = redisTemplate.opsForValue().setIfAbsent(redisKey, IN_PROGRESS, LOCK_TIMEOUT);
        return Boolean.TRUE.equals(acquired);
    }

    public <T> Optional<T> getCachedResponse(String key, Class<T> responseType){
        String redisKey = "idempotency:" + key;
        String val = redisTemplate.opsForValue().get(redisKey);

        if(val == null || IN_PROGRESS.equals(val)){
            return Optional.empty();
        }
        try {
            return Optional.of(objectMapper.readValue(val, responseType));
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize cached response for key {}", key, e);
            return Optional.empty();
        }
    }

    public void complete(String key, Object response){
        String redisKey = "idempotency:" + key;
        try {
            String json = objectMapper.writeValueAsString(response);
            redisTemplate.opsForValue().set(redisKey, json, COMPLETED_TIMEOUT);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize response for key {}", key, e);
        }
    }

    public void unlock(String key){
        redisTemplate.delete("idempotency:" + key);
    }

}
