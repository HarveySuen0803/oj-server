package com.harvey.oj.model.dto.question;

import com.harvey.oj.model.domain.JudgeCase;
import com.harvey.oj.model.domain.JudgeConfig;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class QuestionEditDto implements Serializable {
    private Long id;

    private String title;

    private String content;

    private List<String> tags;

    private String answer;

    private List<JudgeCase> judgeCase;

    private JudgeConfig judgeConfig;

    @Serial
    private static final long serialVersionUID = 1L;
}