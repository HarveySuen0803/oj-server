package com.harvey.oj.model.dto.user;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;

@Data
public class UserSigninDto implements Serializable {
    private String userAccount;

    private String userPassword;
    
    @Serial
    private static final long serialVersionUID = 1L;
}
