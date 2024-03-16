package com.harvey.oj.sandbox;

import com.harvey.oj.constant.JudgeConstant;
import com.harvey.oj.constant.QuestionSubmitConstant;
import com.harvey.oj.model.domain.JudgeConfig;
import com.harvey.oj.model.domain.JudgeInfo;
import com.harvey.oj.model.dto.judge.ExecuteCodeDto;
import com.harvey.oj.model.vo.judge.ExecuteCodeVo;
import org.springframework.stereotype.Component;

@Component
public class ExampleCodeSandbox implements CodeSandBox {
    @Override
    public ExecuteCodeVo executeCode(ExecuteCodeDto executeCodeDto) {
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeConstant.ACCEPTED);
        judgeInfo.setMemory(100L);
        judgeInfo.setTime(100L);
        
        ExecuteCodeVo executeCodeVo = new ExecuteCodeVo();
        executeCodeVo.setOutputList(executeCodeDto.getInputList());
        executeCodeVo.setMessage("The test pass");
        executeCodeVo.setStatus(QuestionSubmitConstant.SUCCESS);
        executeCodeVo.setJudgeInfo(judgeInfo);
        
        return executeCodeVo;
    }
}
