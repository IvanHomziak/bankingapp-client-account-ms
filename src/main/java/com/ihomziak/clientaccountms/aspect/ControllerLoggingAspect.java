package com.ihomziak.clientaccountms.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ControllerLoggingAspect {

    @Pointcut("execution(* com.ihomziak.clientaccountms.controller.*.*(..))")
    public void controllerMethods() {}

    // Log before executing a controller method
    @Before("controllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        log.info("Incoming request: {} {} - Args: {}",
                getHttpMethod(signature),
                getRequestPath(signature),
                Arrays.toString(joinPoint.getArgs()));
    }

    // Log execution time and return response details
    @Around("controllerMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        log.info("Executed {} in {} ms - Response: {}",
                joinPoint.getSignature().toShortString(), executionTime, result);
        return result;
    }

    // Log exceptions thrown in controller methods
    @AfterThrowing(pointcut = "controllerMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("Exception in {} - Message: {}",
                joinPoint.getSignature().toShortString(), ex.getMessage());
    }

    // Extract HTTP Method from annotations
    private String getHttpMethod(MethodSignature signature) {
        if (signature.getMethod().isAnnotationPresent(GetMapping.class)) return "GET";
        if (signature.getMethod().isAnnotationPresent(PostMapping.class)) return "POST";
        if (signature.getMethod().isAnnotationPresent(DeleteMapping.class)) return "DELETE";
        if (signature.getMethod().isAnnotationPresent(PatchMapping.class)) return "PATCH";
        if (signature.getMethod().isAnnotationPresent(PutMapping.class)) return "PUT";
        return "UNKNOWN";
    }

    // Extract request path from annotations
    private String getRequestPath(MethodSignature signature) {
        if (signature.getMethod().isAnnotationPresent(GetMapping.class))
            return signature.getMethod().getAnnotation(GetMapping.class).value()[0];
        if (signature.getMethod().isAnnotationPresent(PostMapping.class))
            return signature.getMethod().getAnnotation(PostMapping.class).value()[0];
        if (signature.getMethod().isAnnotationPresent(DeleteMapping.class))
            return signature.getMethod().getAnnotation(DeleteMapping.class).value()[0];
        if (signature.getMethod().isAnnotationPresent(PatchMapping.class))
            return signature.getMethod().getAnnotation(PatchMapping.class).value()[0];
        if (signature.getMethod().isAnnotationPresent(PutMapping.class))
            return signature.getMethod().getAnnotation(PutMapping.class).value()[0];
        return "UNKNOWN";
    }
}