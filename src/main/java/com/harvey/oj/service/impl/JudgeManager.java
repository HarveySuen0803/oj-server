package com.harvey.oj.service.impl;

import com.harvey.oj.constant.QuestionConstant;
import com.harvey.oj.model.domain.JudgeInfo;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

@Component
public class JudgeManager {
    @Resource
    private JudgeStrategyOfDefault judgeStrategyOfDefault;
    @Resource
    private JudgeStrategyOfJava judgeStrategyOfJava;
    
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        String language = judgeContext.getLanguage();
        if (language.equals(QuestionConstant.JAVA)) {
            return judgeStrategyOfJava.doJudge(judgeContext);
        } else {
            return judgeStrategyOfDefault.doJudge(judgeContext);
        }
    }
}
