package com.harvey.oj.model.vo.judge;

import com.harvey.oj.model.domain.JudgeInfo;
import lombok.Data;

import java.util.List;

@Data
public class ExecuteCodeVo {
    private List<String> outputList;
    
    private String message;
    
    private Integer status;
    
    private JudgeInfo judgeInfo;
}
