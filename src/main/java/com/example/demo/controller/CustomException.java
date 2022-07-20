package com.example.demo.controller;

import com.example.demo.exception.AuthException;
import com.example.demo.exception.BlogException;
import com.example.demo.exception.CustomExceptionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class CustomException extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity authExceptionHandler(AuthException exception) {
        log.error(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomExceptionResponse(exception.getMessage()));
    }

    @ExceptionHandler(BlogException.class)
    public ResponseEntity blogExceptionHandler(BlogException exception) {
        log.error(exception.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new CustomExceptionResponse(exception.getMessage()));
    }
}
