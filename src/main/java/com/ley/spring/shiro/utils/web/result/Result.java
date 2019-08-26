package com.ley.spring.shiro.utils.web.result;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 统一API响应结果封装
 */
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Result {

    /**
     * response code
     **/
    private int code;

    /**
     * response message
     **/
    private String message;

    /**
     * response data
     **/
    private Object data;

    public Result setCode(ResultCode resultCode) {
        this.code = resultCode.code();
        return this;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Result setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public Result setData(Object data) {
        this.data = data;
        return this;
    }

}
