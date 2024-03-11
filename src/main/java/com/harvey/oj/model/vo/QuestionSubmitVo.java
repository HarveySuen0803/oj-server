package com.harvey.oj.model.vo;

import cn.hutool.json.JSONUtil;
import com.harvey.oj.model.domain.JudgeInfo;
import com.harvey.oj.model.domain.QuestionSubmit;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class QuestionSubmitVo implements Serializable {
    private Long id;

    private String language;

    private String code;

    private JudgeInfo judgeInfo;

    private Integer status;

    private Long questionId;

    private Long userId;

    private Date createTime;

    private Date updateTime;

    private UserVo userVO;

    private QuestionVo questionVO;
    
    @Serial
    private static final long serialVersionUID = 1L;
    
    public static QuestionSubmit voToObj(QuestionSubmitVo questionSubmitVO) {
        if (questionSubmitVO == null) {
            return null;
        }
        QuestionSubmit questionSubmit = new QuestionSubmit();
        BeanUtils.copyProperties(questionSubmitVO, questionSubmit);
        JudgeInfo judgeInfoObj = questionSubmitVO.getJudgeInfo();
        if (judgeInfoObj != null) {
            questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfoObj));
        }
        return questionSubmit;
    }

    public static QuestionSubmitVo objToVo(QuestionSubmit questionSubmit) {
        if (questionSubmit == null) {
            return null;
        }
        QuestionSubmitVo questionSubmitVO = new QuestionSubmitVo();
        BeanUtils.copyProperties(questionSubmit, questionSubmitVO);
        String judgeInfoStr = questionSubmit.getJudgeInfo();
        questionSubmitVO.setJudgeInfo(JSONUtil.toBean(judgeInfoStr, JudgeInfo.class));
        return questionSubmitVO;
    }
}