package com.online.exam.exception;

import org.springframework.security.core.AuthenticationException;

public class QAppException extends AuthenticationException {
    public QAppException(String msg){
        super(msg);
    }
    public QAppException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
