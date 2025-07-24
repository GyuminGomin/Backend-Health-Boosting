package com.test.healthboosting.loginapi.controller;

import com.test.healthboosting.loginapi.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/signup/*")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService ss;

    // DB로 처리하는 방식으로 가자
    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerification(@RequestParam String email) {

        String code = ss.generateRandomCode(); // 6자리 숫자
        ss.saveVerificationCode(email, code);

        // 실제 구현 시 이메일 전송 로직 추가
        // emailService.send(email, code);

        return ResponseEntity.ok("Verificatino Code Sent");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestParam String email, @RequestParam String code) {

        boolean result = ss.verifyCode(email, code);
        return result
                ? ResponseEntity.ok("Email Verified.")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired code.");

    }
}
