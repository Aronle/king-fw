package com.mars.fw.web.reponse;

/**
 * @Author King
 * @create 2020/4/20 11:52
 */
public enum KingCode implements IRCode {


    /**
     * 状态码定义
     */
    FAULT(9999, "失败"),
    SUCCESS(10000, "成功"),
    LOGIN_SUCCESS(10001, "登录成功"),
    LOGOUT_SUCCESS(10002, "登录失败"),
    PASS_WRONG(10002, "密码错误"),
    USER_NOT_FIND(10003, "用户不存在"),
    CAPTCHA_TIMEOUT(10004, "验证码失效"),
    CAPTCHA_ERROR(10005, "验证码错误"),
    LOGIN_LOCK(10006, "该账号已被关闭"),
    SMS_EXCEPTION(10007, "短信登录异常"),
    TOKEN_NOT_EMPTY(90000, "token不能为空"),
    TOKEN_ERROR(90001, "token校验失败"),
    TOKEN_EXCEPTION(90002, "token校验异常"),
    URL_EXPIRED(90003, "访问的URL过期"),
    DEFAULT_EXCEPTION(99999, "系统异常");

    final int code;
    final String message;

    @Override
    public int code() {
        return this.code;
    }

    @Override
    public String message() {
        return this.message;
    }

    private KingCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}


