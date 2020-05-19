package com.mars.fw.web.reponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.io.Serializable;

/**
 * @Author King
 * @create 2020/4/20 11:24
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class King<T> implements Serializable {

    private static final long serialVersionUID = -2466174531203439862L;

    public static final Integer SUCCESS_CODE = 1000;
    public static final String SUCCESS_MSG = "操作成功";

    private int code;
    private String msg;
    private T data;


    public King() {
        this(SUCCESS_CODE, SUCCESS_MSG, null);
    }

    public King(IRCode irCode) {
        this(irCode.code(), irCode.message(), null);
    }

    public King(IRCode irCode, T data) {
        this(irCode.code(), irCode.message(), data);
    }

    public King(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public static <T> King<T> success(IRCode irCode, T data) {
        return new King(KingCode.SUCCESS, data);
    }

    public static <T> King<T> success(String message) {
        return new King(KingCode.SUCCESS, message);
    }

    public static <T> King<T> fail(IRCode irCode, T data) {
        return new King(KingCode.FAULT, data);
    }

    public static <T> King<T> fail(String message) {
        return new King(KingCode.FAULT, message);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
