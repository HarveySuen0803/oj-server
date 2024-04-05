package com.harvey.oj.service.impl;

import cn.hutool.json.JSONUtil;
import com.harvey.oj.common.ErrorCode;
import com.harvey.oj.constant.QuestionSubmitConstant;
import com.harvey.oj.exception.BusinessException;
import com.harvey.oj.model.domain.*;
import com.harvey.oj.model.dto.judge.ExecuteCodeDto;
import com.harvey.oj.model.vo.judge.ExecuteCodeVo;
import com.harvey.oj.sandbox.ExampleCodeSandbox;
import com.harvey.oj.service.JudgeService;
import com.harvey.oj.service.QuestionService;
import com.harvey.oj.service.QuestionSubmitService;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JudgeServiceImpl implements JudgeService {
    @Resource
    private QuestionService questionService;
    @Resource
    private QuestionSubmitService questionSubmitService;
    @Resource
    private ExampleCodeSandbox exampleCodeSandbox;
    @Resource
    private JudgeManager judgeManager;
    
    @Override
    public void doJudge(Long questionSubmitId) {
        QuestionSubmit questionSubmit = questionSubmitService.getById(questionSubmitId);
        if (questionSubmit == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Not found the question_submit");
        }
        
        Question question = questionService.getById(questionSubmit.getQuestionId());
        if (question == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "Not found the question");
        }
        
        if (!questionSubmit.getStatus().equals(QuestionSubmitConstant.WAITING)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "Waiting for judgement");
        }
        
        // Update the question_submit status to running
        if (!questionSubmitService.lambdaUpdate()
            .set(QuestionSubmit::getStatus, QuestionSubmitConstant.RUNNING)
            .update()) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to update the question_submit's status");
        }
        
        // Get the input_case list and the output_case list
        List<JudgeCase> judgeCaseList = JSONUtil.toList(question.getJudgeCase(), JudgeCase.class);
        List<String> inputList = judgeCaseList.stream().map(JudgeCase::getInput).toList();
        List<String> outputList = judgeCaseList.stream().map(JudgeCase::getOutput).toList();
        
        // Execute the code with sandbox and get the execution result
        ExecuteCodeDto executeCodeDto = new ExecuteCodeDto();
        executeCodeDto.setLanguage(questionSubmit.getLanguage());
        executeCodeDto.setCode(questionSubmit.getCode());
        executeCodeDto.setInputList(inputList);
        executeCodeDto.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        ExecuteCodeVo executeCodeVo = exampleCodeSandbox.executeCode(executeCodeDto);
        
        // Judge the question_submit with strategy pattern
        JudgeContext judgeContext = new JudgeContext();
        judgeContext.setOutputList(outputList);
        judgeContext.setOutputListOfExecution(executeCodeVo.getOutputList());
        judgeContext.setJudgeInfoOfExecution(executeCodeVo.getJudgeInfo());
        judgeContext.setJudgeConfig(JSONUtil.toBean(question.getJudgeConfig(), JudgeConfig.class));
        judgeContext.setLanguage(questionSubmit.getLanguage());
        JudgeInfo judgeInfo = judgeManager.doJudge(judgeContext);
        
        // Update the question_submit
        questionSubmit.setStatus(QuestionSubmitConstant.SUCCESS);
        questionSubmit.setJudgeInfo(JSONUtil.toJsonStr(judgeInfo));
        if (!questionSubmitService.updateById(questionSubmit)) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to update the question_submit's judge_info and status");
        }
    }
}
