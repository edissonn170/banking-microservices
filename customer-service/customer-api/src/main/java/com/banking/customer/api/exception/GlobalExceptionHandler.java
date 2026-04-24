package com.banking.customer.api.exception;

import com.banking.customer.application.exception.BusinessException;
import com.banking.customer.application.exception.CustomerAlreadyExistsException;
import com.banking.customer.application.exception.ResourceNotFoundException;
import com.banking.customer.dto.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Mono;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.springframework.web.server.ServerWebInputException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public Mono<ResponseEntity<Response<Void>>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        log.error("Resource not found: {}", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Response.notFound(ex.getMessage())));
    }

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<Response<Void>>> handleBusinessException(BusinessException ex) {
        log.error("Business error: {}", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.badRequest(ex.getMessage())));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Response<Void>>> handleValidationException(WebExchangeBindException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getDefaultMessage())
                .collect(Collectors.toList());
        log.error("Validation errors: {}", errors);
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.validationError(errors)));
    }

    @ExceptionHandler(CustomerAlreadyExistsException.class)
    public Mono<ResponseEntity<Response<Void>>> handleCustomerAlreadyExistsException(CustomerAlreadyExistsException ex) {
        log.error("Customer already exists: {}", ex.getMessage());
        return Mono.just(ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(Response.conflict(ex.getMessage())));
    }

    @ExceptionHandler(ServerWebInputException.class)
    public Mono<ResponseEntity<Response<Void>>> handleServerWebInputException(ServerWebInputException ex) {
        log.error("Invalid request input: {}", ex.getMessage());
        String message = findInvalidFormatException(ex)
                .map(this::buildTypeErrorMessage)
                .orElse("Invalid request body: check field types and format");
        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Response.badRequest(message)));
    }

    private Optional<InvalidFormatException> findInvalidFormatException(Throwable ex) {
        return Optional.ofNullable(ex)
                .map(Throwable::getCause)
                .filter(cause -> cause instanceof InvalidFormatException)
                .map(cause -> (InvalidFormatException) cause)
                .or(() -> Optional.ofNullable(ex)
                        .map(Throwable::getCause)
                        .map(Throwable::getCause)
                        .filter(cause -> cause instanceof InvalidFormatException)
                        .map(cause -> (InvalidFormatException) cause));
    }

    private String buildTypeErrorMessage(InvalidFormatException ex) {
        String field = ex.getPath().stream()
                .reduce((first, last) -> last)
                .map(ref -> ref.getFieldName())
                .orElse("unknown");
        String expectedType = ex.getTargetType().getSimpleName().toLowerCase();
        return String.format("Field '%s' must be a valid %s", field, expectedType);
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Response<Void>>> handleGenericException(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Response.internalServerError("An unexpected error occurred")));
    }
}
