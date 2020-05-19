package com.mars.fw.web.exception;

import com.mars.fw.web.reponse.ExceptionCode;
import com.mars.fw.web.reponse.IRCode;
import com.mars.fw.web.reponse.King;

/**
 * @Author King
 * @create 2020/4/20 15:35
 */
public class KingException extends BaseException {

    /**
     * http 编码
     */
    private int httpStatus = 200;
    /**
     * 错误类型编号
     */
    private IRCode code;
    /**
     * 错误携带数据
     */
    private Object data;


    public KingException(IRCode code) {
        super(code.message());
        this.code = code;
    }

    public KingException(String message) {
        super(message);
        this.code = ExceptionCode.DEFAULT_EXCEPTION;
    }

    public KingException(String message, Object data) {
        super(message);
        this.code = ExceptionCode.DEFAULT_EXCEPTION;
        this.data = data;
    }

    public KingException(String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = ExceptionCode.DEFAULT_EXCEPTION;
        this.data = data;
    }

    public KingException(String message, Throwable cause) {
        super(message, cause);
        this.code = ExceptionCode.DEFAULT_EXCEPTION;
    }


    public KingException(Object data) {
        super(ExceptionCode.DEFAULT_EXCEPTION.message());
        this.data = data;
        this.code = ExceptionCode.DEFAULT_EXCEPTION;
    }

    public KingException(IRCode code, String message) {
        super(message);
        this.code = code;
    }

    public KingException(IRCode code, String message, Object data) {
        super(message);
        this.code = code;
        this.data = data;
    }

    public KingException(IRCode code, String message, Object data, Throwable cause) {
        super(message, cause);
        this.code = code;
        this.data = data;
    }

    public KingException(IRCode code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public KingException(IRCode code, Object data) {
        super(code.message());
        this.data = data;
        this.code = code;
    }


    public int getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(int httpStatus) {
        this.httpStatus = httpStatus;
    }

    public IRCode getCode() {
        return code;
    }

    public void setCode(IRCode code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public King transferResponse() {
        King response = new King(this.getCode());
        response.setMsg(this.getMessage());
        return response;
    }
}
