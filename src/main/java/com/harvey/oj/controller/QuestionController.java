package com.harvey.oj.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.harvey.oj.common.Result;
import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.common.ResultUtil;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.exception.ThrowUtils;
import com.harvey.oj.model.domain.Question;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.question.QuestionAddDto;
import com.harvey.oj.model.dto.question.QuestionEditDto;
import com.harvey.oj.model.dto.question.QuestionPageDto;
import com.harvey.oj.model.dto.question.QuestionUpdDto;
import com.harvey.oj.model.vo.QuestionVo;
import com.harvey.oj.service.QuestionService;
import com.harvey.oj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/question")
public class QuestionController {
    @Resource
    private QuestionService questionService;
    
    @Resource
    private UserService userService;
    
    @PostMapping
    public Result<Long> addQuestion(@RequestBody QuestionAddDto questionAddDto) {
        System.out.println("-----------------------------------------------------");
        System.out.println(questionAddDto);
        System.out.println("-----------------------------------------------------");
        
        Question question = BeanUtil.copyProperties(questionAddDto, Question.class);
        question.setTags(JSONUtil.toJsonStr(questionAddDto.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(questionAddDto.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionAddDto.getJudgeConfig()));
        
        questionService.validQuestion(question, true);
        User loginUser = userService.getUserSigninInfo();
        question.setUserId(loginUser.getId());
        questionService.save(question);
        return ResultUtil.success(question.getId());
    }
    
    @GetMapping("/{id}")
    public Result<Question> getQuestionById(@PathVariable long id) {
        Question question = questionService.getById(id);
        return ResultUtil.success(question);
    }
    
    // @GetMapping("/get/vo")
    // public Result<QuestionVo> getQuestionVoById(long id) {
    //     if (id <= 0) {
    //         throw new BusinessException(ErrorCode.PARAMS_ERROR);
    //     }
    //     Question question = questionService.getById(id);
    //     if (question == null) {
    //         throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
    //     }
    //     return ResultUtil.success(questionService.getQuestionVo(question));
    // }
    
    @DeleteMapping("/{id}")
    public Result<Boolean> delQuestion(@PathVariable Long id) {
        boolean result = questionService.removeById(id);
        return ResultUtil.success(result);
    }
    
    @PutMapping
    public Result<Boolean> updQuestion(@RequestBody QuestionUpdDto questionUpdDto) {
        Question question = BeanUtil.copyProperties(questionUpdDto, Question.class);
        question.setTags(JSONUtil.toJsonStr(questionUpdDto.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(questionUpdDto.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionUpdDto.getJudgeConfig()));
        questionService.validQuestion(question, false);

        boolean result = questionService.updateById(question);
        return ResultUtil.success(result);
    }
    
    // @PostMapping("/list/page/vo")
    // public Result<Page<QuestionVo>> getQuestionVoPage(@RequestBody QuestionPageDto questionQueryRequest) {
    //     long current = questionQueryRequest.getCurrent();
    //     long size = questionQueryRequest.getPageSize();
    //     ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
    //     Page<Question> questionPage = questionService.page(
    //         new Page<>(current, size),
    //         questionService.getQueryWrapper(questionQueryRequest)
    //     );
    //     return ResultUtil.success(questionService.getQuestionVoPage(questionPage));
    // }
    
    // @PostMapping("/my/list/page/vo")
    // public Result<Page<QuestionVo>> getMyQuestionVoPage(@RequestBody QuestionPageDto questionPageDto) {
    //     if (questionPageDto == null) {
    //         throw new BusinessException(ErrorCode.PARAMS_ERROR);
    //     }
    //     User loginUser = userService.getLoginUser();
    //     questionPageDto.setUserId(loginUser.getId());
    //     long current = questionPageDto.getCurrent();
    //     long size = questionPageDto.getPageSize();
    //     ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
    //     Page<Question> questionPage = questionService.page(
    //         new Page<>(current, size),
    //         questionService.getQueryWrapper(questionPageDto)
    //     );
    //
    //     return ResultUtil.success(questionService.getQuestionVoPage(questionPage));
    // }
    
    @PostMapping("/page")
    public Result<Page<QuestionVo>> getQuestionPage(@RequestBody QuestionPageDto questionPageDto) {
        long current = questionPageDto.getCurrent();
        long size = questionPageDto.getPageSize();
        Page<Question> questionPage = questionService.page(
            new Page<>(current, size),
            questionService.getQueryWrapper(questionPageDto)
        );
        Page<QuestionVo> questionVoPage = new Page<>(questionPage.getCurrent(), questionPage.getSize(), questionPage.getTotal());
        List<QuestionVo> questionVoList = questionPage.getRecords().stream().map((question) -> {
            QuestionVo questionVo = new QuestionVo();
            questionVo.setId(question.getId());
            questionVo.setTitle(question.getTitle());
            questionVo.setTags(JSONUtil.parseArray(question.getTags()).toList(String.class));
            questionVo.setSubmitNum(question.getSubmitNum());
            questionVo.setAcceptedNum(question.getAcceptedNum());
            return questionVo;
        }).toList();
        questionVoPage.setRecords(questionVoList);
        return ResultUtil.success(questionVoPage);
    }
    
    @PostMapping("/edit")
    public Result<Boolean> editQuestion(@RequestBody QuestionEditDto questionEditDto) {
        if (questionEditDto == null || questionEditDto.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Question question = BeanUtil.copyProperties(questionEditDto, Question.class);
        question.setTags(JSONUtil.toJsonStr(questionEditDto.getTags()));
        question.setJudgeCase(JSONUtil.toJsonStr(questionEditDto.getJudgeCase()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionEditDto.getJudgeConfig()));
        
        questionService.validQuestion(question, false);
        User loginUser = userService.getUserSigninInfo();
        long id = questionEditDto.getId();
        Question oldQuestion = questionService.getById(id);
        ThrowUtils.throwIf(oldQuestion == null, ErrorCode.NOT_FOUND_ERROR);
        if (!oldQuestion.getUserId().equals(loginUser.getId()) && !userService.isAdmin(loginUser)) {
            throw new BusinessException(ErrorCode.Forbidden);
        }
        boolean result = questionService.updateById(question);
        
        return ResultUtil.success(result);
    }

}
