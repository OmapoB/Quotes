package ru.omarov.quotes.aspects;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * ru.omarov.quotes.*.*get*(..))")
    private void setAllGet() {
    }

    @Pointcut("execution(* ru.omarov.quotes.*.*(..))")
    private void setAll() {
    }

    @Before("setAllGet()")
    public void beforeGet(JoinPoint joinPoint) {
        log.trace(joinPoint.getSignature().toString());
    }

    @After("setAllGet()")
    public void afterGet(JoinPoint joinPoint) {
        log.trace(joinPoint.getSignature().toString());
    }

    @AfterThrowing(pointcut = "setAll()", throwing = "exception")
    public void afterError(Throwable exception) {
        log.error(exception.getMessage());
    }
}
