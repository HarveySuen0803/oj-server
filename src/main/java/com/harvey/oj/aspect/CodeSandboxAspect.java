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
public class CodeSandboxAspect {
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));
    
    @Around("execution(* com.harvey.oj.sandbox.*.executeCode(..))")
    public Object codeSandboxAround(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> tarClass = joinPoint.getTarget().getClass();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Object[] reqArgs = joinPoint.getArgs();
        
        threadPool.submit(() -> {
            log.info("Req - Code Sandbox: {}, Arguments: {}", tarClass.getName(), reqArgs);
        });
        
        Object res = joinPoint.proceed();
        
        threadPool.submit(() -> {
            log.info("Rep - Code Sandbox: {}, Result: {}", tarClass.getName(), res);
        });
        return res;
    }
}
