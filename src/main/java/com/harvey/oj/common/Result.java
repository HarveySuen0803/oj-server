package com.harvey.oj.common;

import java.io.Serializable;
import lombok.Data;

@Data
public class Result<T> implements Serializable {

    private int code;

    private T data;

    private String msg;

    public Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public Result(int code, T data) {
        this(code, data, "");
    }

    public Result(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMsg());
    }
}
