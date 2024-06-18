package com.spring.webflux.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Set;

@Component
public class GlobalRequestValidator {
    private final Validator validator;

    public GlobalRequestValidator(Validator validator) {
        this.validator = validator;
    }

    public <T> Mono<T> validateRequest(T requestObj) {
        if (requestObj == null)
            return Mono.error(new IllegalArgumentException("Request object must not be null"));

        Set<ConstraintViolation<T>> violations = validator.validate(requestObj);

        if (!violations.isEmpty())
            return Mono.error(new ConstraintViolationException(violations));

        return Mono.just(requestObj);
    }

}
