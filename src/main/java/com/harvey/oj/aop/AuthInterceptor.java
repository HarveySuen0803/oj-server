package com.harvey.oj.aop;

import com.harvey.oj.annotation.AuthCheck;
import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.service.UserService;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthInterceptor {
    @Resource
    private UserService userService;
    
    // Todo: Rewrite the login function with token (2)
    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String role = authCheck.role();
        User loginUser = userService.getUserSigninInfo();
        if (StringUtils.isNotBlank(role) && !loginUser.getUserRole().equals(role)) {
            throw new BusinessException(ErrorCode.Forbidden);
        }
        return joinPoint.proceed();
    }
}

