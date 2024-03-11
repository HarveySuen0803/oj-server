package com.harvey.oj.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
public class User implements Serializable {
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    
    private String userAccount;
    
    private String userPassword;
    
    private String unionId;
    
    private String mpOpenId;
    
    private String userName;
    
    private String userAvatar;
    
    private String userProfile;
    
    private String userRole;
    
    private Date createTime;
    
    private Date updateTime;
    
    private Integer isDelete;
    
    @Serial
    private static final long serialVersionUID = 1L;
}