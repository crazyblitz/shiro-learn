package com.ley.spring.shiro.utils.web.result;

/**
 * 响应码枚举,参考HTTP状态码的语义
 *
 * @see org.springframework.http.HttpStatus
 */
public enum ResultCode {

    SUCCESS(200),//成功
    FAIL(400),//失败
    UNAUTHORIZED(401),//未认证(签名错误)
    NOT_FOUND(404),//接口不存在
    INTERNAL_SERVER_ERROR(500),//服务器内部错误
    VALID(-1); //登录验证不通过

    private final int code;

    ResultCode(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
