package com.online.exam.exception;

import org.springframework.security.core.AuthenticationException;

public class InvalidUsernameException extends AuthenticationException {
    public InvalidUsernameException(String msg) {
        super(msg);
    }

    public InvalidUsernameException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

