package com.test.healthboosting.common.config;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice("com.test.healthboosting.*.controller")
public class CommonExceptionHandler {

    // 운영환경에서는 상세 메시지 숨기고, 개발환경에서만 상세 표시
    private String getMessage(Exception e) {
        String activeProfile = System.getProperty("my.env");
        if ("dev".equals(activeProfile)) {
            return e.getMessage();
        } else {
            return "서버에서 오류가 발생했습니다.";
        }
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", e.getMessage());
        return ResponseEntity.ok(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", getMessage(e));
        e.printStackTrace();
        return ResponseEntity.status(500).body(response); // HttpStatus.INTERNAL_SERVER_ERROR
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", "유효하지 않은 요청입니다.");
        e.printStackTrace();
        return ResponseEntity.status(400).body(response); // HttpStatus.BAD_REQUEST
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<Map<String, Object>> handleAccessDeniedException(AccessDeniedException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", "권한이 없습니다.");
        return ResponseEntity.status(403).body(response); // HttpStatus.FORBIDDEN
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Map<String, Object>> handleAuthenticationException(AuthenticationException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("error", true);
        response.put("message", "로그인이 필요합니다.");
        return ResponseEntity.status(401).body(response); // HttpStatus.UNAUTHORIZED
    }
}
