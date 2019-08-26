package com.ley.spring.shiro.utils.web.result;


/**
 * result entity
 **/
public final class ResultEntity {

    private ResultEntity() {
    }

    public static Result success(String message, Object data) {
        return Result.builder().code(ResultCode.SUCCESS.code()).message(message).data(data).build();
    }

    /**
     * 结果返回成功,适合于表示状态的结果返回
     **/
    public static Result success(String message) {
        return Result.builder().code(ResultCode.SUCCESS.code()).message(message).data(true).build();
    }


    public static Result error(String message, Object data) {
        return Result.builder().code(ResultCode.FAIL.code()).message(message).data(data).build();
    }


    /**
     * 结果返回失败,适合于表示状态的结果返回
     **/
    public static Result error(String message) {
        return Result.builder().code(ResultCode.FAIL.code()).message(message).data(false).build();
    }


    /**
     * 验证未通过
     **/
    public static Result unauthorized(String message) {
        return Result.builder().code(ResultCode.UNAUTHORIZED.code()).message(message).data(false).build();
    }

}
