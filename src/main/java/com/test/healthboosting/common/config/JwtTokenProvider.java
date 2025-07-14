package com.test.healthboosting.common.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String SECRET_KEY;
    private static final long TOKEN_VALIDITY = 3600000L; // 1시간
    private final ObjectMapper objectMapper = new ObjectMapper();

    public String createToken(String userId, List<String> roles) {
        Instant now = Instant.now(); // 현재 시간을 Instant로 가져옴
        return Jwts.builder()
                .claim("sub", userId) // setSubject 대신 sub 클레임 사용
                .claim("roles", roles) // 사용자 정의 클레임 (권한 부여)
                .claim("iat", Date.from(now)) // setIssuedAt 대신 iat 클레임 사용
                .claim("exp", Date.from(now.plusMillis(TOKEN_VALIDITY))) // setExpiration 대신 exp 클레임 사용
                .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // 서명 키 설정
                .compact();
    }

    public Authentication getAuthentication(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String userId = claims.getSubject();
            // roles를 JSON 문자열로 변환 후 List<String>으로 역직렬화
            Object rolesObject = claims.get("roles");
            List<String> roles = (rolesObject != null)
                    ? objectMapper.convertValue(rolesObject, new TypeReference<>() {})
                    : List.of(); // roles가 없을 경우 빈 리스트로 처리

            return new UsernamePasswordAuthenticationToken(
                    userId,
                    null,
                    roles.stream()
                         .map(SimpleGrantedAuthority::new)
                         .toList());
        } catch (JwtException | IllegalArgumentException e) {
            throw new RuntimeException("Invalid JWT token", e);
        }
    }
}
