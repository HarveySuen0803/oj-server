package com.harvey.oj.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class Question implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String content;
    
    private String tags;
    
    private String answer;
    
    private Integer submitNum;
    
    private Integer acceptedNum;
    
    private String judgeCase;
    
    private String judgeConfig;
    
    private Integer thumbNum;
    
    private Integer favourNum;
    
    private Date createTime;
    
    private Date updateTime;
    
    private Integer isDelete;
    
    @Serial
    private static final long serialVersionUID = 1L;
}