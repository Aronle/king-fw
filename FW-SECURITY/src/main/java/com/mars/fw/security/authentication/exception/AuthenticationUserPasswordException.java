package com.mars.fw.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @description:
 * @author: aron
 * @date: 2019-07-04 14:12
 */
public class AuthenticationUserPasswordException extends AuthenticationException {

    public AuthenticationUserPasswordException(String msg) {
        super(msg);
    }

    public AuthenticationUserPasswordException(String msg, Throwable t) {
        super(msg, t);
    }
}