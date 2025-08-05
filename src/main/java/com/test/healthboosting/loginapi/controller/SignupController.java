package com.test.healthboosting.loginapi.controller;

import com.test.healthboosting.loginapi.dto.EmailRequest;
import com.test.healthboosting.loginapi.dto.MemberDTO;
import com.test.healthboosting.loginapi.service.SignupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/signup/*")
@RequiredArgsConstructor
public class SignupController {

    private final SignupService ss;

    /**
     * Sends a verification email with a unique code to the provided email address.
     * The generated code is valid for 5 minutes and is saved for verification purposes.
     *
     * @param emailRequest the request object containing the target email address
     * @return a ResponseEntity indicating the success or failure of the operation
     */
    @PostMapping("/send-verification")
    public ResponseEntity<?> sendVerification(@RequestBody EmailRequest emailRequest) throws Exception {

        String code = ss.generateRandomCode(); // 6자리 숫자
        ss.saveEmailVerificationCode(emailRequest.getEmail(), code);

        ss.sendVerificationEmail(emailRequest.getEmail(), code);

        return ResponseEntity.ok("Verification Code Sent");
    }

    /**
     * Verifies the email code provided by the user. The code and email are checked against the stored
     * code in the system to determine if the verification is successful. If the verification is successful,
     * an "Email Verified" message is returned. If the verification fails, an "Invalid or expired code"
     * message is returned with an HTTP status of UNAUTHORIZED.
     *
     * @param emailRequest the request object containing the email and the code for verification
     * @return a ResponseEntity containing the result of the verification process
     */
    @PostMapping("/verify-code")
    public ResponseEntity<?> verifyCode(@RequestBody EmailRequest emailRequest) throws Exception {

        boolean result = ss.verifyEmailCode(emailRequest.getEmail(), emailRequest.getCode());
        return result
                ? ResponseEntity.ok("Email Verified.")
                : ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired code.");

    }

    /**
     * Cancels an ongoing email verification process for the provided email address by deleting the
     * corresponding verification code from the system.
     *
     * @param email the email address for which the verification process should be canceled
     * @return a ResponseEntity containing a success message indicating the cancellation of the verification process
     */
    @DeleteMapping("/cancel-verification")
    public ResponseEntity<?> cancelVerification(@RequestParam String email) throws Exception {
        ss.deleteVerificationCode(email);
        return ResponseEntity.ok("Verification Code Canceled.");
    }

    // 회원가입 요청
    @PostMapping("/regist")
    public ResponseEntity<?> regist(
            @RequestPart("form") MemberDTO form,
            @RequestPart(value="profileImage", required = false) MultipartFile profile
    ) throws Exception {

        ss.regist(form, profile);

        return ResponseEntity.ok("ok");
    }

}
