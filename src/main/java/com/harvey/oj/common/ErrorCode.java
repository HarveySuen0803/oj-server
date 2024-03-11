package com.harvey.oj.common;

import lombok.Getter;

@Getter
public enum ErrorCode {
    PARAMS_ERROR(400, "Request parameter error"),
    NOT_LOGIN_ERROR(401, "Not sign in"),
    Forbidden(401, "Forbidden"),
    NOT_FOUND_ERROR(404, "Request data does not exist"),
    SYSTEM_ERROR(500, "System internal exception"),
    OPERATION_ERROR(501, "Operation failed");

    private final int code;

    private final String msg;
    
    ErrorCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
