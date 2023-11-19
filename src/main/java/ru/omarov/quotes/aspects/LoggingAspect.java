package ru.omarov.quotes.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {
    @Before("Pointcuts.allGetMethods()")
    public void beforeGet(JoinPoint joinPoint) {
        log.trace(joinPoint.getSignature().toString());
    }

    @After("Pointcuts.allGetMethods()")
    public void afterGet(JoinPoint joinPoint) {
        log.trace(joinPoint.getSignature().toString());
    }

    @AfterThrowing(pointcut = "Pointcuts.allMethods()", throwing = "exception")
    public void afterError(Throwable exception) {
        log.error(exception.getMessage());
    }
}
