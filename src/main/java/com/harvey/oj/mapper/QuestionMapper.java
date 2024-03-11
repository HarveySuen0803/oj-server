package com.harvey.oj.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.harvey.oj.model.domain.Question;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface QuestionMapper extends BaseMapper<Question> {
}




