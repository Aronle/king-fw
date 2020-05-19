package com.mars.fw.encrypt;

import com.mars.fw.web.exception.KingException;
import com.mars.fw.web.reponse.IRCode;

/**
 * @Author King
 * @create 2020/4/23 14:05
 */
public class EncryptException extends KingException {
    public EncryptException(IRCode code) {
        super(code);
    }

    public EncryptException(String message) {
        super(message);
    }

    public EncryptException(String message, Object data) {
        super(message, data);
    }

    public EncryptException(String message, Object data, Throwable cause) {
        super(message, data, cause);
    }

    public EncryptException(String message, Throwable cause) {
        super(message, cause);
    }

    public EncryptException(Object data) {
        super(data);
    }

    public EncryptException(IRCode code, String message) {
        super(code, message);
    }

    public EncryptException(IRCode code, String message, Object data) {
        super(code, message, data);
    }

    public EncryptException(IRCode code, String message, Object data, Throwable cause) {
        super(code, message, data, cause);
    }

    public EncryptException(IRCode code, String message, Throwable cause) {
        super(code, message, cause);
    }

    public EncryptException(IRCode code, Object data) {
        super(code, data);
    }
}
