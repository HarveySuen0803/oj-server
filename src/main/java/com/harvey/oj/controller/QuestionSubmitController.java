package com.harvey.oj.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.oj.common.Result;
import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.common.ResultUtil;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.model.domain.QuestionSubmit;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitAddDto;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitQueryDto;
import com.harvey.oj.model.vo.QuestionSubmitVo;
import com.harvey.oj.service.QuestionSubmitService;
import com.harvey.oj.service.UserService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class QuestionSubmitController {
    @Resource
    private UserService userService;
    
    @Resource
    private QuestionSubmitService questionSubmitService;
    
    @PostMapping("/question_submit/do")
    public Result<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddDto questionSubmitAddDto) {
        if (questionSubmitAddDto == null || questionSubmitAddDto.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getUserSigninInfo();
        long questionSubmitId = questionSubmitService.addQuestionSubmit(questionSubmitAddDto, loginUser);
        return ResultUtil.success(questionSubmitId);
    }
    
    @PostMapping("/question_submit/list/page")
    public Result<Page<QuestionSubmitVo>> getQuestionSubmitVoPage(@RequestBody QuestionSubmitQueryDto questionSubmitQueryRequest) {
        long current = questionSubmitQueryRequest.getCurrent();
        long size = questionSubmitQueryRequest.getPageSize();
        Page<QuestionSubmit> questionSubmitPage = questionSubmitService.page(new Page<>(current, size), questionSubmitService.getQueryWrapper(questionSubmitQueryRequest));
        User loginUser = userService.getUserSigninInfo();
        return ResultUtil.success(questionSubmitService.getQuestionSubmitVoPage(questionSubmitPage, loginUser));
    }
}
