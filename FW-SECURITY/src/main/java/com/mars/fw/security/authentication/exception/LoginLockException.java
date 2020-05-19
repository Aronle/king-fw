package com.mars.fw.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginLockException extends AuthenticationException {
    public LoginLockException(String msg, Throwable t) {
        super(msg, t);
    }

    public LoginLockException(String msg) {
        super(msg);
    }
}
