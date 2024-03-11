package com.harvey.oj.model.dto.question;

import com.harvey.oj.constant.CommonConstant;
import com.harvey.oj.model.dto.common.PageDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Data
public class QuestionPageDto implements Serializable {
    private Long id;

    private String title;

    private String content;

    private List<String> tags;

    private String answer;

    private Long userId;
    
    private int current = 1;
    
    private int pageSize = 10;
    
    private String sortField;
    
    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
    
    @Serial
    private static final long serialVersionUID = 1L;
}