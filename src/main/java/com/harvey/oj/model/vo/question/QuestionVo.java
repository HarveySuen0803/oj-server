package com.harvey.oj.model.vo.question;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.harvey.oj.model.domain.JudgeConfig;
import com.harvey.oj.model.domain.Question;
import com.harvey.oj.model.vo.user.UserVo;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class QuestionVo implements Serializable {
    private Long id;
    
    private String title;
    
    private String content;
    
    private List<String> tags;
    
    private Integer submitNum;
    
    private Integer acceptedNum;
    
    private JudgeConfig judgeConfig;
    
    private Integer thumbNum;
    
    private Integer favourNum;
    
    private Long userId;
    
    private Date createTime;
    
    private Date updateTime;
    
    private UserVo userVo;
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    public static Question voToObj(QuestionVo questionVo) {
        if (questionVo == null) {
            return null;
        }
        Question question = BeanUtil.copyProperties(questionVo, Question.class);
        question.setTags(JSONUtil.toJsonStr(questionVo.getTags()));
        question.setJudgeConfig(JSONUtil.toJsonStr(questionVo.getJudgeConfig()));
        return question;
    }
    
    public static QuestionVo objToVo(Question question) {
        if (question == null) {
            return null;
        }
        QuestionVo questionVo = BeanUtil.copyProperties(question, QuestionVo.class);
        questionVo.setTags(JSONUtil.toList(question.getTags(), String.class));
        questionVo.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        return questionVo;
    }
}