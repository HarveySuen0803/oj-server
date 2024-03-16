package com.harvey.oj.model.dto.judge;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExecuteCodeDto {
    private List<String> inputList;
    
    private String code;
    
    private String language;
}
