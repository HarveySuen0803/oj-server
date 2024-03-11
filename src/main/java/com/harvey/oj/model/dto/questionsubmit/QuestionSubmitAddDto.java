package com.harvey.oj.model.dto.questionsubmit;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class QuestionSubmitAddDto implements Serializable {
    private String language;

    private String code;

    private Long questionId;
    
    @Serial
    private static final long serialVersionUID = 1L;
}