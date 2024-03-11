package com.harvey.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.user.UserQueryDto;
import com.harvey.oj.model.vo.UserSigninVo;
import com.harvey.oj.model.vo.UserVo;

import java.util.List;

public interface UserService extends IService<User> {
    long register(String userAccount, String userPassword, String checkPassword);

    UserSigninVo signin(String userAccount, String userPassword);

    User getUserSigninInfo();

    User getLoginUserPermitNull();

    boolean isAdmin();

    boolean isAdmin(User user);

    boolean userLogout();

    UserSigninVo getUserSigninVo(User user);

    UserVo getUserVo(User user);

    List<UserVo> getUserVo(List<User> userList);

    QueryWrapper<User> getQueryWrapper(UserQueryDto userQueryRequest);
}
