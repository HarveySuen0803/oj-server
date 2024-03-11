package com.harvey.oj.model.dto.user;

import com.harvey.oj.model.dto.common.PageDto;

import java.io.Serial;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryDto extends PageDto implements Serializable {
    private Long id;

    private String unionId;

    private String mpOpenId;

    private String userName;

    private String userProfile;

    private String userRole;
    
    @Serial
    private static final long serialVersionUID = 1L;
}