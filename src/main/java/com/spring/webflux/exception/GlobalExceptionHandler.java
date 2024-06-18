package com.spring.webflux.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class GlobalExceptionHandler extends AbstractErrorWebExceptionHandler {

    private final Map<Class<? extends Throwable>, Function<Throwable, Mono<ServerResponse>>> exceptionHandlers = new HashMap<>();

    public GlobalExceptionHandler(ErrorAttributes errorAttributes, WebProperties.Resources resources,
                                  ApplicationContext applicationContext, ServerCodecConfigurer codecConfigurer) {
        super(errorAttributes, resources, applicationContext);
        this.setMessageReaders(codecConfigurer.getReaders());
        this.setMessageWriters(codecConfigurer.getWriters());

        // Register exception handlers
        exceptionHandlers.put(ResponseStatusException.class, this::handleResponseStatusException);
        exceptionHandlers.put(ConstraintViolationException.class, this::handleConstraintViolationException);
        exceptionHandlers.put(IllegalArgumentException.class, this::handleIllegalArgumentException);
        exceptionHandlers.put(NotFoundException.class, this::handleNotFoundException);
    }

    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::customErrorResponse);
    }

    private Mono<ServerResponse> customErrorResponse(ServerRequest request) {
        Throwable errorObj = getError(request);
        Function<Throwable, Mono<ServerResponse>> handler = exceptionHandlers.getOrDefault(errorObj.getClass(), this::handleDefaultError);
        return handler.apply(errorObj);
    }

    private Mono<ServerResponse> handleResponseStatusException(Throwable ex) {
        ResponseStatusException responseStatusException = (ResponseStatusException) ex;
        return createServerErrorResponse(responseStatusException.getStatusCode(), responseStatusException.getReason());
    }

    private Mono<ServerResponse> handleConstraintViolationException(Throwable ex) {
        ConstraintViolationException constraintViolationException = (ConstraintViolationException) ex;
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", "Validation failed for request");
        errorAttributes.put("details", extractConstraintViolationMessages(constraintViolationException));
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> handleIllegalArgumentException(Throwable ex) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", "Illegal argument provided");
        errorAttributes.put("details", ex.getMessage());
        return ServerResponse.status(HttpStatus.BAD_REQUEST)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> handleNotFoundException(Throwable ex) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", "Resource not found");
        errorAttributes.put("details", ex.getMessage());
        return ServerResponse.status(HttpStatus.NOT_FOUND)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> handleDefaultError(Throwable ex) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", "Server error");
        errorAttributes.put("details", ex.getMessage());
        return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private Mono<ServerResponse> createServerErrorResponse(HttpStatusCode status, String message) {
        Map<String, Object> errorAttributes = new HashMap<>();
        errorAttributes.put("message", message != null ? message : "Server error");
        return ServerResponse.status(status)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(errorAttributes);
    }

    private List<String> extractConstraintViolationMessages(ConstraintViolationException ex) {
        return ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
    }
}
