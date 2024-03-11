package com.harvey.oj.model.dto.common;

import com.harvey.oj.constant.CommonConstant;
import lombok.Data;

@Data
public class PageDto {
    private int current = 1;

    private int pageSize = 10;

    private String sortField;

    private String sortOrder = CommonConstant.SORT_ORDER_ASC;
}
