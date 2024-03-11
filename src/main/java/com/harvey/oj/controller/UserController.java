package com.harvey.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.oj.common.Result;
import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.common.ResultUtil;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.exception.ThrowUtils;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.common.DeleteDto;
import com.harvey.oj.model.dto.user.*;
import com.harvey.oj.model.vo.UserSigninVo;
import com.harvey.oj.model.vo.UserVo;
import com.harvey.oj.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.harvey.oj.service.impl.UserServiceImpl.SALT;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
    
    @Resource
    private UserService userService;
    
    // @PostMapping("/register")
    // public Result<Long> register(@RequestBody UserRegisterDto userRegisterDto) {
    //     if (userRegisterDto == null) {
    //         throw new BusinessException(ErrorCode.PARAMS_ERROR);
    //     }
    //     String userAccount = userRegisterDto.getUserAccount();
    //     String userPassword = userRegisterDto.getUserPassword();
    //     String checkPassword = userRegisterDto.getCheckPassword();
    //     if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
    //         return null;
    //     }
    //     long result = userService.register(userAccount, userPassword, checkPassword);
    //     return ResultUtil.success(result);
    // }
    
    @PostMapping("/signin")
    public Result<UserSigninVo> signin(@RequestBody UserSigninDto userSigninDto) {
        if (userSigninDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userSigninDto.getUserAccount();
        String userPassword = userSigninDto.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserSigninVo userSigninVo = userService.signin(userAccount, userPassword);
        return ResultUtil.success(userSigninVo);
    }
    
    // @PostMapping("/logout")
    // public Result<Boolean> logout() {
    //     boolean result = userService.userLogout();
    //     return ResultUtil.success(result);
    // }
    
    @GetMapping("/signin/info")
    public Result<UserSigninVo> getUserSigninInfo() {
        User user = userService.getUserSigninInfo();
        return ResultUtil.success(userService.getUserSigninVo(user));
    }
    
    @PostMapping("/add")
    public Result<Long> addUser(@RequestBody UserAddDto userAddDto) {
        if (userAddDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userAddDto, user);
        String defaultPassword = "12345678";
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + defaultPassword).getBytes());
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtil.success(user.getId());
    }
    
    @GetMapping("/get")
    public Result<User> getUserById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtil.success(user);
    }
    
    @GetMapping("/get/vo")
    public Result<UserVo> getUserVoById(long id) {
        Result<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtil.success(userService.getUserVo(user));
    }
    
    @PostMapping("/page")
    public Result<Page<User>> getUserPage(@RequestBody UserQueryDto userQueryRequest) {
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(
            new Page<>(current, size),
            userService.getQueryWrapper(userQueryRequest)
        );
        return ResultUtil.success(userPage);
    }
    
    @PostMapping("/page/vo")
    public Result<Page<UserVo>> getUserVoPage(@RequestBody UserQueryDto userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = userQueryRequest.getCurrent();
        long size = userQueryRequest.getPageSize();
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<User> userPage = userService.page(
            new Page<>(current, size),
            userService.getQueryWrapper(userQueryRequest)
        );
        Page<UserVo> userVoPage = new Page<>(current, size, userPage.getTotal());
        List<UserVo> userVo = userService.getUserVo(userPage.getRecords());
        userVoPage.setRecords(userVo);
        return ResultUtil.success(userVoPage);
    }
    
    @PostMapping("/upd/my")
    public Result<Boolean> updMyUser(@RequestBody UserUpdMyDto userUpdMyDto) {
        if (userUpdMyDto == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getUserSigninInfo();
        User user = new User();
        BeanUtils.copyProperties(userUpdMyDto, user);
        user.setId(loginUser.getId());
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtil.success(true);
    }
    
    @PostMapping("/del")
    public Result<Boolean> delUser(@RequestBody DeleteDto deleteDto) {
        if (deleteDto == null || deleteDto.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean b = userService.removeById(deleteDto.getId());
        return ResultUtil.success(b);
    }
    
    @PostMapping("/upd")
    public Result<Boolean> updUser(@RequestBody UserUpdDto userUpdDto) {
        if (userUpdDto == null || userUpdDto.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdDto, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtil.success(true);
    }
}
