package com.spring.webflux.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Object> handleResponseStatusException(ResponseStatusException ex) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("status", ex.getStatusCode());
        errorAttributes.put("message", ex.getReason());
        return ResponseEntity.status(ex.getStatusCode()).body(errorAttributes);
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Object>> handleBindException(WebExchangeBindException ex) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("message", "Validation failed");
        errorAttributes.put("details", getBindingErrors(ex));
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON).body(errorAttributes));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<Object>> handleServerWebInputException(ServerWebInputException ex) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("message", ex.getMessage());
        return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON).body(errorAttributes));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolationException(ConstraintViolationException ex) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", "Validation failed for request");
        errorAttributes.put("details", extractConstraintViolationMessages(ex));
        return ResponseEntity.badRequest().body(errorAttributes);
    }

    private List<String> getBindingErrors(WebExchangeBindException ex) {
        return ex.getFieldErrors().stream()
                .map(fieldError -> fieldError.getField() + ": " + fieldError.getDefaultMessage())
                .collect(Collectors.toList());
    }

    private List<String> extractConstraintViolationMessages(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
    }
}