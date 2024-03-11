package com.harvey.oj.model.dto.common;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
public class DeleteDto implements Serializable {
    private Long id;

    @Serial
    private static final long serialVersionUID = 1L;
}