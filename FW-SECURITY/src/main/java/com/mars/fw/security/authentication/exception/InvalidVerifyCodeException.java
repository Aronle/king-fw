package com.mars.fw.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidVerifyCodeException extends AuthenticationException {
    public InvalidVerifyCodeException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidVerifyCodeException(String msg) {
        super(msg);
    }
}
