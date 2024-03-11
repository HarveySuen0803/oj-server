package com.harvey.oj.common;

public class ResultUtil {
    public static <T> Result<T> success(T data) {
        return new Result<>(200, data, "ok");
    }

    public static Result error(ErrorCode errorCode) {
        return new Result<>(errorCode);
    }

    public static Result error(int code, String message) {
        return new Result(code, null, message);
    }

    public static Result error(ErrorCode errorCode, String message) {
        return new Result(errorCode.getCode(), null, message);
    }
}
