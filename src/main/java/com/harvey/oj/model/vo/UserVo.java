package com.harvey.oj.model.vo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UserVo implements Serializable {
    private Long id;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private String userRole;

    private Date createTime;
    
    @Serial
    private static final long serialVersionUID = 1L;
}