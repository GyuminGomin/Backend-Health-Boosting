package com.test.healthboosting.loginapi.controller;

import com.test.healthboosting.loginapi.dto.EmailRequest;
import com.test.healthboosting.loginapi.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/signup/*")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService ss;

    // DB로 처리하는 방식으로 가자
    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerification(@RequestBody EmailRequest emailRequest) {

        String code = ss.generateRandomCode(); // 6자리 숫자
        ss.saveVerificationCode(emailRequest.getEmail(), code);

        ss.sendVerificationEmail(emailRequest.getEmail(), code);

        return ResponseEntity.ok("Verification Code Sent");
    }

    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody EmailRequest emailRequest) {

        boolean result = ss.verifyCode(emailRequest.getEmail(), emailRequest.getCode());
        return result
                ? ResponseEntity.ok("Email Verified.")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired code.");

    }

    @DeleteMapping("/cancel-verification")
    public ResponseEntity<?> cancelVerification(@RequestParam String email) {
        ss.deleteVerificationCode(email);
        return ResponseEntity.ok("Verification Code Canceled.");
    }

}
