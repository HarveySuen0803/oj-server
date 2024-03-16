package com.harvey.oj.controller;

import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.common.Result;
import com.harvey.oj.common.ResultUtil;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitAddDto;
import com.harvey.oj.service.QuestionSubmitService;
import com.harvey.oj.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/question/submit")
public class QuestionSubmitController {
    @Resource
    private UserService userService;
    
    @Resource
    private QuestionSubmitService questionSubmitService;
    
    @PostMapping
    public Result<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddDto questionSubmitAddDto) {
        if (questionSubmitAddDto == null || questionSubmitAddDto.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getUserSigninInfo();
        long questionSubmitId = questionSubmitService.addQuestionSubmit(questionSubmitAddDto, user);
        return ResultUtil.success(questionSubmitId);
    }
}
