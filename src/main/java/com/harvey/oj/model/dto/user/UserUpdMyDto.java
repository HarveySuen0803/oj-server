package com.harvey.oj.model.dto.user;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
public class UserUpdMyDto implements Serializable {
    private String userName;

    private String userAvatar;

    private String userProfile;
    
    @Serial
    private static final long serialVersionUID = 1L;
}