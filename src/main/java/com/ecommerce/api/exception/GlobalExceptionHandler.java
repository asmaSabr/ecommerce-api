package com.ecommerce.api.exception;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex, Locale locale) {
        String message = messageSource.getMessage(
                "RESOURCE_NOT_FOUND", null, "Resource not found", locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("RESOURCE_NOT_FOUND", message, null));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleParamsInvalid(
            MethodArgumentTypeMismatchException ex, Locale locale) {
        String message = messageSource.getMessage(
                "PARAMS_NOT_VALID", null, "Invalid parameter", locale);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErrorResponse("PARAMS_NOT_VALID", message, null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBodyInvalid(
            MethodArgumentNotValidException ex, Locale locale) {
        String message = messageSource.getMessage(
                "BODY_NOT_VALID", null, "Validation failed", locale);
        List<String> details = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResponse("BODY_NOT_VALID", message, details));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> handlePathNotFound(
            NoSuchElementException ex, Locale locale) {
        String message = messageSource.getMessage(
                "PATH_NOT_FOUND", null, "Path not found", locale);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("PATH_NOT_FOUND", message, null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleServerError(
            Exception ex, Locale locale) {
        String message = messageSource.getMessage(
                "SERVER_ERROR", null, "Internal error", locale);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("SERVER_ERROR", message, null));
    }
}
