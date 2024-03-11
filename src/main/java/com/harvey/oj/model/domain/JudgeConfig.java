package com.harvey.oj.model.domain;

import lombok.Data;

@Data
public class JudgeConfig {
    private Long timeLimit;

    private Long memoryLimit;

    private Long stackLimit;
}
