package com.harvey.oj.service;

import com.harvey.oj.model.domain.JudgeInfo;
import com.harvey.oj.service.impl.JudgeContext;

public interface JudgeStrategy {
    JudgeInfo doJudge(JudgeContext judgeContext);
}
