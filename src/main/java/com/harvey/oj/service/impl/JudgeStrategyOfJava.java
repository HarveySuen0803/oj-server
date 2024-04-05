package com.harvey.oj.service.impl;

import com.harvey.oj.constant.JudgeConstant;
import com.harvey.oj.model.domain.JudgeConfig;
import com.harvey.oj.model.domain.JudgeInfo;
import com.harvey.oj.service.JudgeStrategy;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JudgeStrategyOfJava implements JudgeStrategy {
    @Override
    public JudgeInfo doJudge(JudgeContext judgeContext) {
        List<String> outputListOfExecution = judgeContext.getOutputListOfExecution();
        JudgeInfo judgeInfoOfExecution = judgeContext.getJudgeInfoOfExecution();
        List<String> outputList = judgeContext.getOutputList();
        JudgeConfig judgeConfig = judgeContext.getJudgeConfig();
        
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMemory(judgeInfoOfExecution.getMemory());
        judgeInfo.setTime(judgeInfoOfExecution.getTime());
        
        // Judge the output list
        if (outputList.size() != outputListOfExecution.size()) {
            judgeInfo.setMessage(JudgeConstant.WRONG_ANSWER);
            return judgeInfo;
        }
        for (int i = 0; i < outputList.size(); i++) {
            if (!outputList.get(i).equals(outputListOfExecution.get(i))) {
                judgeInfo.setMessage(JudgeConstant.WRONG_ANSWER);
                return judgeInfo;
            }
        }
        
        // Todo: Rewrite the judgement
        // Judge the memory and time limitation
        if (judgeInfoOfExecution.getMemory() > judgeConfig.getMemoryLimit()) {
            judgeInfo.setMessage(JudgeConstant.MEMORY_LIMIT_EXCEEDED);
            return judgeInfo;
        }
        if (judgeInfoOfExecution.getTime() > judgeConfig.getTimeLimit()) {
            judgeInfo.setMessage(JudgeConstant.TIME_LIMIT_EXCEEDED);
            return judgeInfo;
        }
        
        // AC !!!
        judgeInfo.setMessage(JudgeConstant.ACCEPTED);
        return judgeInfo;
    }
}
