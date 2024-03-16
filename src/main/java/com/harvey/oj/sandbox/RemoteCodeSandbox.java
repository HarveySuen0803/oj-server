package com.harvey.oj.sandbox;

import com.harvey.oj.model.dto.judge.ExecuteCodeDto;
import com.harvey.oj.model.vo.judge.ExecuteCodeVo;
import org.springframework.stereotype.Component;

@Component
public class RemoteCodeSandbox implements CodeSandBox {
    @Override
    public ExecuteCodeVo executeCode(ExecuteCodeDto executeCodeDto) {
        return null;
    }
}
