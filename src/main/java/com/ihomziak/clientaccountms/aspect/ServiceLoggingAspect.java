package com.ihomziak.clientaccountms.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Aspect
@Component
public class ServiceLoggingAspect {

    @Pointcut("execution(* com.ihomziak.clientaccountms.service.impl.*.*(..))")
    public void serviceMethods() {}

    // Log before executing a service method
    @Before("serviceMethods()")
    public void logBefore(JoinPoint joinPoint) {
        log.info("Executing service method: {} - Args: {}",
                joinPoint.getSignature().toShortString(), Arrays.toString(joinPoint.getArgs()));
    }

    // Log execution time and return response details
    @Around("serviceMethods()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result = joinPoint.proceed();
        long executionTime = System.currentTimeMillis() - start;

        log.info("Service method {} executed in {} ms - Result: {}",
                joinPoint.getSignature().toShortString(), executionTime, result);
        return result;
    }

    // Log exceptions thrown in service methods
    @AfterThrowing(pointcut = "serviceMethods()", throwing = "ex")
    public void logException(JoinPoint joinPoint, Exception ex) {
        log.error("Exception in service method {} - Message: {}",
                joinPoint.getSignature().toShortString(), ex.getMessage());
    }

    // Log method exit
    @AfterReturning(pointcut = "serviceMethods()", returning = "result")
    public void logAfterReturning(JoinPoint joinPoint, Object result) {
        log.info("Service method {} executed successfully - Result: {}",
                joinPoint.getSignature().toShortString(), result);
    }
}