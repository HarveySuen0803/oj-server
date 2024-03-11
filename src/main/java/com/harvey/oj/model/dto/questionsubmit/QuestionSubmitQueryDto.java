package com.harvey.oj.model.dto.questionsubmit;

import com.harvey.oj.model.dto.common.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
public class QuestionSubmitQueryDto extends PageDto implements Serializable {
    private String language;

    private Integer status;

    private Long questionId;

    private Long userId;

    @Serial
    private static final long serialVersionUID = 1L;
}