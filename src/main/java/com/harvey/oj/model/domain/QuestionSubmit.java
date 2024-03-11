package com.harvey.oj.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class QuestionSubmit implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    private Long questionId;

    private Long userId;

    private String language;

    private String code;

    private String judgeInfo;

    private Integer status;

    private Date createTime;

    private Date updateTime;

    private Integer isDelete;

    @Serial
    private static final long serialVersionUID = 1L;
}