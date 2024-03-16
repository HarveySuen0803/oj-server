package com.harvey.oj.sandbox;

import com.harvey.oj.constant.CodeSandboxConstant;

public class CodeSandboxFactory {
    public static CodeSandBox create(String type) {
        if (CodeSandboxConstant.EXAMPLE.equals(type)) {
            return new ExampleCodeSandbox();
        }
        if (CodeSandboxConstant.REMOTE.equals(type)) {
            return new RemoteCodeSandbox();
        }
        return new DefaultCodeSandbox();
    }
}
