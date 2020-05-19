package com.mars.fw.web.reponse;

/**
 * @Author King
 * @create 2020/4/20 11:52
 */
public enum ExceptionCode implements IRCode {


    /**
     * 异常状态码定义
     */
    DEFAULT_EXCEPTION(9000, "系统异常");

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

    private ExceptionCode(final int code, final String message) {
        this.code = code;
        this.message = message;
    }
}


