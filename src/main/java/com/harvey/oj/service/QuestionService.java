package com.harvey.oj.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.harvey.oj.model.domain.Question;
import com.harvey.oj.model.dto.question.QuestionPageDto;
import com.harvey.oj.model.vo.QuestionVo;

public interface QuestionService extends IService<Question> {
    void validQuestion(Question question, boolean b);
    
    QueryWrapper<Question> getQueryWrapper(QuestionPageDto questionQueryRequest);
    
    QuestionVo getQuestionVo(Question question);
    
    Page<QuestionVo> getQuestionVoPage(Page<Question> questionPage);
}
