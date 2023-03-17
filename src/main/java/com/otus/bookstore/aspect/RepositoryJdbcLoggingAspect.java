package com.otus.bookstore.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class RepositoryJdbcLoggingAspect {
    private static final Logger logger = LoggerFactory.getLogger(RepositoryJdbcLoggingAspect.class);

    @Around("execution(* com.otus.bookstore.repository.*Repository.*(..))")
    public Object logMethodCall(ProceedingJoinPoint joinPoint) throws Throwable {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        String methodName = joinPoint.getSignature().getName();
        Object[] args = joinPoint.getArgs();

        logger.info("Entering {}.{}() with arguments {}", className, methodName, args);

        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            logger.error("Exception thrown in {}.{}() with cause = '{}'", className, methodName, e.getCause());
            throw e;
        }

        logger.info("Exiting {}.{}() with result {}", className, methodName, result);

        return result;
    }
}
