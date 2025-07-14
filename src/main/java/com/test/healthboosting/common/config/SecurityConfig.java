package com.test.healthboosting.common.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http,
            SecurityFilterChain oauth2SecurityFilterChain,
            CustomOAuth2SuccessHandler customOAuth2SuccessHandler,
            CustomOAuth2FailureHandler customOAuth2FailureHandler,
            JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers(
                                // Swagger 관련 경로 허용
                                "/v3/api-docs/**",
                                "/swagger-ui/**",
                                "/swagger-ui.html",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/api/**"
                        ).permitAll()  // Swagger는 모두 허용
                        .anyRequest().authenticated()  // 나머지는 인증 필요
                )
                .csrf(AbstractHttpConfigurer::disable)
                // 기본 로그인 폼 비활성화
                .formLogin(AbstractHttpConfigurer::disable)
                // 세션 없이 Stateless로 인증
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // 예외 핸들러 설정 (401 응답 반환)
                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json");
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                    response.getWriter().write("{\"message\":\"Unauthorized\"}");
                }))
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("http://localhost:5173") // 로그인 페이지
                        .successHandler(customOAuth2SuccessHandler) // 로그인 성공 시 JWT 발급 등 처리
                        .failureHandler(customOAuth2FailureHandler)); // 로그인 실패 시 처리

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class); // 토큰을 헤더에서 읽어 인증 처리까지


        return http.build();
    }
}