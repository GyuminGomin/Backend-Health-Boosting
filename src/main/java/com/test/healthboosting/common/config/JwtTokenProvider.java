package com.test.healthboosting.common.config;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class JwtTokenProvider {

    @Value("${jwt.secret-key}")
    private String secretKeyString;
    private SecretKey secretKey;
    private JwtParser jwtParser;
    private static final long TOKEN_VALIDITY = 3600000L; // 1시간
    private final ObjectMapper objectMapper = new ObjectMapper();

    @PostConstruct
    public void init() {
        secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKeyString));
        jwtParser = Jwts.parser().verifyWith(secretKey).build();
    }

    public String createToken(String userId, List<String> roles) {
        Instant now = Instant.now(); // 현재 시간을 Instant로 가져옴
        return Jwts.builder()
                .subject(userId)
                .expiration(Date.from(now.plusMillis(TOKEN_VALIDITY)))
                .issuedAt(Date.from(now))
                .claim("roles", roles) // 사용자 정의 클레임 (권한 부여)
                .signWith(secretKey) // 서명 키 설정
                .compact();
    }

    public Authentication getAuthentication(String token) {
        try {
            Claims claims = jwtParser
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
