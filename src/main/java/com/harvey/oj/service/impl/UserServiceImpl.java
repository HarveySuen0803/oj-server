package com.harvey.oj.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.constant.CommonConstant;
import com.harvey.oj.constant.UserConstant;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.mapper.UserMapper;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.user.UserQueryDto;
import com.harvey.oj.model.vo.user.UserSigninVo;
import com.harvey.oj.model.vo.user.UserVo;
import com.harvey.oj.service.UserService;
import com.harvey.oj.util.SqlUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    
    public static final String SALT = "oj";
    
    @Override
    public long register(String userAccount, String userPassword, String checkPassword) {
        if (StrUtil.isBlank(userAccount) || StrUtil.isBlank(userPassword) || StrUtil.isBlank(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Parameter should not be empty");
        }
        if (userAccount.length() < 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User account too short");
        }
        if (userPassword.length() < 2 || checkPassword.length() < 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "User password too short");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "The two passwords entered do not match");
        }
        synchronized (userAccount.intern()) {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("user_account", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "Duplicate account");
            }
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Registration failed, database error");
            }
            return user.getId();
        }
    }
    
    @Override
    public UserSigninVo signin(String userAccount, String userPassword) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (StrUtil.isBlank(userAccount) || StrUtil.isBlank(userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Parameter should not be empty");
        }
        if (userAccount.length() < 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account error");
        }
        if (userPassword.length() < 2) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Password error");
        }
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserAccount, userAccount);
        queryWrapper.eq(User::getUserPassword, encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        if (user == null) {
            log.info("user signin failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Account does not exist or password is wrong");
        }
        request.getSession().setAttribute(UserConstant.USER_SIGNIN_STATUS, user);
        return this.getUserSigninVo(user);
    }
    
    // Todo: Rewrite the login function with token (3)
    @Override
    public User getUserSigninInfo() {
        // HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        // Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS);
        // User currentUser = (User) userObj;
        // if (currentUser == null || currentUser.getId() == null) {
        //     throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        // }
        // long userId = currentUser.getId();
        // currentUser = this.getById(userId);
        // if (currentUser == null) {
        //     throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        // }
        // return currentUser;
        return this.getById(1);
    }
    
    @Override
    public User getLoginUserPermitNull() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object userObj = request.getSession().getAttribute(UserConstant.USER_SIGNIN_STATUS);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            return null;
        }
        long userId = currentUser.getId();
        return this.getById(userId);
    }
    
    @Override
    public boolean isAdmin() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        Object userObj = request.getSession().getAttribute(UserConstant.USER_SIGNIN_STATUS);
        User user = (User) userObj;
        return isAdmin(user);
    }
    
    @Override
    public boolean isAdmin(User user) {
        return user != null && UserConstant.ADMIN_ROLE.equals(user.getUserRole());
    }
    
    @Override
    public boolean userLogout() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        if (request.getSession().getAttribute(UserConstant.USER_SIGNIN_STATUS) == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");
        }
        request.getSession().removeAttribute(UserConstant.USER_SIGNIN_STATUS);
        return true;
    }
    
    @Override
    public UserSigninVo getUserSigninVo(User user) {
        if (user == null) {
            return null;
        }
        UserSigninVo userSigninVo = new UserSigninVo();
        BeanUtils.copyProperties(user, userSigninVo);
        return userSigninVo;
    }
    
    @Override
    public UserVo getUserVo(User user) {
        if (user == null) {
            return null;
        }
        UserVo userVo = new UserVo();
        BeanUtils.copyProperties(user, userVo);
        return userVo;
    }
    
    @Override
    public List<UserVo> getUserVo(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVo).collect(Collectors.toList());
    }
    
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryDto userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Null request parameter");
        }
        Long id = userQueryRequest.getId();
        String unionId = userQueryRequest.getUnionId();
        String mpOpenId = userQueryRequest.getMpOpenId();
        String userName = userQueryRequest.getUserName();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(id != null, "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(unionId), "union_id", unionId);
        queryWrapper.eq(StrUtil.isNotBlank(mpOpenId), "mp_open_id", mpOpenId);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "user_role", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "user_profile", userProfile);
        queryWrapper.like(StrUtil.isNotBlank(userName), "user_name", userName);
        queryWrapper.orderBy(SqlUtil.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }
}
