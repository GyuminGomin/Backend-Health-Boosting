package com.test.healthboosting.loginapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final RedisTemplate<String, String> redisTemplate;

    public void saveVerificationCode(String email, String code) {
        // 5분간 유효한 인증 코드 저장
        redisTemplate.opsForValue().set("email:verify:" + email, code, 5, java.util.concurrent.TimeUnit.MINUTES);
    }

    public boolean verifyCode(String email, String code) {
        String redisKey = "email:verify:" + email;
        String storedCode = redisTemplate.opsForValue().get(redisKey);

        // 코드 일치 여부 확인
        if (storedCode != null && storedCode.equals(code)) {
            redisTemplate.delete(redisKey); // 인증 성공 후 삭제
            return true;
        }

        return false;
    }

    public String generateRandomCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }
}
