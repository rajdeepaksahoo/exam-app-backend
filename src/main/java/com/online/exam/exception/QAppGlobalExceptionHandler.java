package com.online.exam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class QAppGlobalExceptionHandler {
    @ExceptionHandler(QAppException.class)
    public ResponseEntity<Map<String,Object>> qAppException(QAppException qAppException){
        Map<String,Object> errorResponse = new HashMap<>();
        errorResponse.put("errorCode",HttpStatus.INTERNAL_SERVER_ERROR.value());
        errorResponse.put("errorReason",qAppException.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
