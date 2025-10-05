package com.example.cosmocats.web;

import com.example.cosmocats.dto.exception.ExceptionResponse;
import com.example.cosmocats.service.exception.NoSuchResourceException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException ex,
            HttpServletRequest request
    ) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        String errorMessage = String.format("Validation failed for object '%s': %s",
                fieldError != null ? fieldError.getObjectName() : "unknown",
                fieldError != null ? fieldError.getDefaultMessage() : "unknown message."
        );

        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Bad Request")
                .message(errorMessage)
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.badRequest()
                .body(exceptionResponse);
    }

    @ExceptionHandler(NoSuchResourceException.class)
    public ResponseEntity<ExceptionResponse> handleNoSuchResourceException(
            NoSuchResourceException ex,
            HttpServletRequest request
    ) {
        ExceptionResponse exceptionResponse = ExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .error("Not Found")
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(exceptionResponse);
    }
}
