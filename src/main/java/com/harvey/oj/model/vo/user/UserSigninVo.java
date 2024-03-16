package com.harvey.oj.model.vo.user;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class UserSigninVo implements Serializable {
    private Long id;

    private String userName;

    private String userAvatar;

    private String userProfile;

    private String userRole;

    private Date createTime;

    private Date updateTime;
    
    @Serial
    private static final long serialVersionUID = 1L;
}