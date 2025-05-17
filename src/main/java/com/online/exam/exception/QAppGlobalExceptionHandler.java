package com.online.exam.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, Object> errors = new HashMap<>();
        List<String> errorMessages = new ArrayList<>();
        errors.put("errorCode",HttpStatus.BAD_REQUEST.value());
        ex.getBindingResult().getFieldErrors().forEach(error ->
//                errors.put("errorReason",(String)errors.getOrDefault("errorReason","")+" \n"+ error.getDefaultMessage())
                errorMessages.add(error.getDefaultMessage())
        );
        errors.put("errorReason",errorMessages);
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }
}
