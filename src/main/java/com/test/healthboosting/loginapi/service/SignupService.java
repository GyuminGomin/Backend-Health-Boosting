package com.test.healthboosting.loginapi.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignupService {

    private final RedisTemplate<String, String> redisTemplate;
    private final JavaMailSender mailSender; // 앱 정보가 틀리면, 자동 빈등록 안됨

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
        redisTemplate.delete(redisKey); // 인증 실패시에도 삭제
        return false;
    }

    public boolean deleteVerificationCode(String email) {
        return redisTemplate.delete("email:verify:" + email);
    }

    public String generateRandomCode() {
        return String.valueOf((int)(Math.random() * 900000) + 100000);
    }

    public void sendVerificationEmail(String to, String code) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("[HealthBoosting 이메일 인증 코드]");
        message.setText("인증 코드: " + code + "\n5분 이내로 입력해주세요.");

        mailSender.send(message);
    }


}
