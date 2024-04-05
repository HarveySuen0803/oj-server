package com.harvey.oj.controller;

import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.common.Result;
import com.harvey.oj.common.ResultUtil;
import com.harvey.oj.constant.QuestionSubmitConstant;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.model.domain.Question;
import com.harvey.oj.model.domain.QuestionSubmit;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitAddDto;
import com.harvey.oj.service.JudgeService;
import com.harvey.oj.service.QuestionService;
import com.harvey.oj.service.QuestionSubmitService;
import com.harvey.oj.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@RestController
@RequestMapping("/question/submit")
public class QuestionSubmitController {
    @Resource
    private UserService userService;
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private QuestionService questionService;
    @Resource
    private JudgeService judgeService;
    
    private ThreadPoolExecutor threadPool = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(100));
    
    @PostMapping
    public Result<Long> addQuestionSubmit(@RequestBody QuestionSubmitAddDto questionSubmitAddDto) {
        if (questionSubmitAddDto == null || questionSubmitAddDto.getQuestionId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = userService.getUserSigninInfo();
        
        // Check the programming language
        String language = questionSubmitAddDto.getLanguage();
        if (language == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "Programming Language Error");
        }
        
        // Check the question
        long questionId = questionSubmitAddDto.getQuestionId();
        Question question = questionService.getById(questionId);
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        
        // Add the question_submit
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(user.getId());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddDto.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionSubmitConstant.WAITING);
        questionSubmit.setJudgeInfo("{}");
        boolean isSaved = questionSubmitService.save(questionSubmit);
        if (!isSaved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Data insertion failed");
        }
        
        // Start the judgement
        Long questionSubmitId = questionSubmit.getId();
        threadPool.submit(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        
        return ResultUtil.success(questionSubmitId);
    }
}
