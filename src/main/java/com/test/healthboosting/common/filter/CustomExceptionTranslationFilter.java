package com.test.healthboosting.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.Http403ForbiddenEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class CustomExceptionTranslationFilter extends OncePerRequestFilter {

    private final AuthenticationEntryPoint authenticationEntryPoint = new Http403ForbiddenEntryPoint();
    private final AccessDeniedHandler accessDeniedHandler = new AccessDeniedHandlerImpl();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            filterChain.doFilter(request, response);
        } catch (AuthenticationException authException) {
            throw authException;  // ControllerAdvice로 넘긴다
        } catch (AccessDeniedException accessDeniedException) {
            throw accessDeniedException;  // ControllerAdvice로 넘긴다
        } catch (RuntimeException ex) {
            throw ex;  // ControllerAdvice로 넘긴다
        }
    }
}
