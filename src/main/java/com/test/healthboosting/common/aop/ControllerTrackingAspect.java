package com.test.healthboosting.common.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ControllerTrackingAspect {

    @Before("execution(* com.test.healthboosting.*.controller..*.*(..))")
    public void trackControllerCall(JoinPoint joinPoint) {
        String controller = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        MDC.put("caller", controller + "#" + method); // 로그백에서 %X{caller}로 사용
    }

    @After("execution(* com.test.healthboosting.*.controller..*.*(..))")
    public void clearMdc() {
        MDC.remove("caller");
    }

    @Before("execution(* com.test.healthboosting.*.service..*.*(..))")
    public void trackServiceCall(JoinPoint joinPoint) {
        String service = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        MDC.put("service", service + "#" + method);
    }

    @After("execution(* com.test.healthboosting.*.service..*.*(..))")
    public void clearServiceMdc() {
        MDC.remove("service");
    }

    @Before("execution(* com.test.healthboosting.*.mapper..*.*(..))")
    public void trackMapperCall(JoinPoint joinPoint) {
        String mapper = joinPoint.getSignature().getDeclaringTypeName();
        String method = joinPoint.getSignature().getName();
        MDC.put("service", mapper + "#" + method);
    }

    @After("execution(* com.test.healthboosting.*.mapper..*.*(..))")
    public void clearMapperMdc() {
        MDC.remove("mapper");
    }
}
