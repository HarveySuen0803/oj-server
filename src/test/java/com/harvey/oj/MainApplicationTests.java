package com.harvey.oj;

import com.harvey.oj.constant.CodeSandboxConstant;
import com.harvey.oj.model.dto.judge.ExecuteCodeDto;
import com.harvey.oj.sandbox.CodeSandBox;
import com.harvey.oj.sandbox.CodeSandboxFactory;
import com.harvey.oj.sandbox.DefaultCodeSandbox;
import jakarta.annotation.Resource;
import org.aspectj.apache.bcel.classfile.Code;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MainApplicationTests {
    @Resource
    private DefaultCodeSandbox defaultCodeSandbox;
    
    @Test
    public void test() {
        System.out.println(defaultCodeSandbox.executeCode(new ExecuteCodeDto()));
    }
}
