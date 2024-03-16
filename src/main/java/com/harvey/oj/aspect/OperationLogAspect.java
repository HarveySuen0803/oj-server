package com.harvey.oj.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Aspect
@Component
public class OperationLogAspect {
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));
    
    @Around("execution(* com.harvey.oj.controller.*.*(..))")
    public Object operationLogAround(ProceedingJoinPoint joinPoint) throws Throwable {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        HttpServletRequest httpServletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
        String reqUri = httpServletRequest.getRequestURI();
        String reqMethod = httpServletRequest.getMethod();
        Object[] reqArgs = joinPoint.getArgs();
        String reqHost = httpServletRequest.getRemoteHost();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String methodName = methodSignature.getMethod().getName();
        
        threadPool.submit(() -> {
            log.info("Req - Uri: [{}] {}, Host: {}, Method Name: {}, Arguments: {}", reqMethod, reqUri, reqHost, methodName, reqArgs);
        });
        
        Object res = joinPoint.proceed();
        
        threadPool.submit(() -> {
            log.info("Rep - Uri: [{}] {}, Host: {}, Method Name: {}, Result: {}", reqMethod, reqUri, reqHost, methodName, res);
        });
        return res;
    }
}
