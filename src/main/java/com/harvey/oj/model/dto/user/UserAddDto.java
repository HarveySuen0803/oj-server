package com.harvey.oj.model.dto.user;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
public class UserAddDto implements Serializable {
    private String userName;

    private String userAccount;

    private String userAvatar;

    private String userRole;
    
    @Serial
    private static final long serialVersionUID = 1L;
}