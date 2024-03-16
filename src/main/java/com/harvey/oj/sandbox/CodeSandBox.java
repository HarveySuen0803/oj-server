package com.harvey.oj.sandbox;

import com.harvey.oj.model.dto.judge.ExecuteCodeDto;
import com.harvey.oj.model.vo.judge.ExecuteCodeVo;

public interface CodeSandBox {
    ExecuteCodeVo executeCode(ExecuteCodeDto executeCodeDto);
}
