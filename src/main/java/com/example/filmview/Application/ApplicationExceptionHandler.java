package com.example.filmview.Application;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ExceptionHandler(value = ApplicationException.class)
    public ResponseEntity<ApplicationErrorResponse> handle(ApplicationException exception){
        return new ResponseEntity<>(new ApplicationErrorResponse(exception.getMessage()), HttpStatusCode.valueOf(exception.getCode()));
    }
}
