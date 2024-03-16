package com.harvey.oj.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.constant.CommonConstant;
import com.harvey.oj.constant.QuestionConstant;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.mapper.QuestionSubmitMapper;
import com.harvey.oj.model.domain.Question;
import com.harvey.oj.model.domain.QuestionSubmit;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitAddDto;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitQueryDto;
import com.harvey.oj.model.vo.QuestionSubmitVo;
import com.harvey.oj.service.JudgeService;
import com.harvey.oj.service.QuestionService;
import com.harvey.oj.service.QuestionSubmitService;
import com.harvey.oj.service.UserService;
import com.harvey.oj.util.SqlUtil;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit> implements QuestionSubmitService {
    @Resource
    private UserService userService;
    
    @Resource
    private JudgeService judgeService;
    
    @Resource
    private QuestionService questionService;
    
    @Override
    public long addQuestionSubmit(QuestionSubmitAddDto questionSubmitAddDto, User user) {
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
        // Save the submission
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setUserId(user.getId());
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setCode(questionSubmitAddDto.getCode());
        questionSubmit.setLanguage(language);
        questionSubmit.setStatus(QuestionConstant.WAITING);
        questionSubmit.setJudgeInfo("{}");
        boolean result = this.save(questionSubmit);
        if (!result) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Data insertion failed");
        }
        // Start the judge
        Long questionSubmitId = questionSubmit.getId();
        CompletableFuture.runAsync(() -> {
            judgeService.doJudge(questionSubmitId);
        });
        
        return questionSubmitId;
    }
    
    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryDto questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if (questionSubmitQueryRequest == null) {
            return queryWrapper;
        }
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();
        
        queryWrapper.eq(StrUtil.isNotBlank(language), "language", language);
        queryWrapper.eq(ObjectUtil.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(ObjectUtil.isNotEmpty(questionId), "question_id", questionId);
        queryWrapper.eq("status", status);
        queryWrapper.eq("is_delete", false);
        queryWrapper.orderBy(SqlUtil.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC), sortField);
        return queryWrapper;
    }
    
    @Override
    public QuestionSubmitVo getQuestionSubmitVo(QuestionSubmit questionSubmit, User loginUser) {
        // Hide the code if the user is not the submitter or the user is not an admin
        QuestionSubmitVo questionSubmitVO = QuestionSubmitVo.objToVo(questionSubmit);
        long userId = loginUser.getId();
        if (userId != questionSubmit.getUserId() && !userService.isAdmin(loginUser)) {
            questionSubmitVO.setCode(null);
        }
        return questionSubmitVO;
    }
    
    @Override
    public Page<QuestionSubmitVo> getQuestionSubmitVoPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        // Hide the code if the user is not the submitter or the user is not an admin
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVo> questionSubmitVoPage = new Page<>(questionSubmitPage.getCurrent(), questionSubmitPage.getSize(), questionSubmitPage.getTotal());
        if (CollUtil.isEmpty(questionSubmitList)) {
            return questionSubmitVoPage;
        }
        List<QuestionSubmitVo> questionSubmitVoList = questionSubmitList.stream()
            .map((questionSubmit) -> getQuestionSubmitVo(questionSubmit, loginUser))
            .toList();
        questionSubmitVoPage.setRecords(questionSubmitVoList);
        return questionSubmitVoPage;
    }
}




