package com.harvey.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.oj.model.domain.QuestionSubmit;
import com.harvey.oj.model.domain.User;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitAddDto;
import com.harvey.oj.model.dto.questionsubmit.QuestionSubmitQueryDto;
import com.harvey.oj.model.vo.questionsubmit.QuestionSubmitVo;

public interface QuestionSubmitService extends IService<QuestionSubmit> {
    long addQuestionSubmit(QuestionSubmitAddDto questionSubmitAddDto, User loginUser);
    
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryDto questionSubmitQueryRequest);
    
    QuestionSubmitVo getQuestionSubmitVo(QuestionSubmit questionSubmit, User loginUser);
    
    Page<QuestionSubmitVo> getQuestionSubmitVoPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
