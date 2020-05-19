package com.mars.fw.security.authentication.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidDataException extends AuthenticationException {
    public InvalidDataException(String msg, Throwable t) {
        super(msg, t);
    }

    public InvalidDataException(String msg) {
        super(msg);
    }
}
