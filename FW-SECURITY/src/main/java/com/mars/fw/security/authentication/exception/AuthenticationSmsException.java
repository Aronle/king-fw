package com.mars.fw.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @description:
 * @author: aron
 * @date: 2019-07-04 14:12
 */
public class AuthenticationSmsException extends AuthenticationException {

    public AuthenticationSmsException(String msg) {
        super(msg);
    }

    public AuthenticationSmsException(String msg, Throwable t) {
        super(msg, t);
    }
}