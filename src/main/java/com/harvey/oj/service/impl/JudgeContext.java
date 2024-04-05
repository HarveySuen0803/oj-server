package com.harvey.oj.service.impl;

import com.harvey.oj.model.domain.JudgeConfig;
import com.harvey.oj.model.domain.JudgeInfo;
import lombok.Data;

import java.util.List;

@Data
public class JudgeContext {
    List<String> outputList;
    
    List<String> outputListOfExecution;
    
    JudgeConfig judgeConfig;
    
    JudgeInfo judgeInfoOfExecution;
    
    String language;
}
