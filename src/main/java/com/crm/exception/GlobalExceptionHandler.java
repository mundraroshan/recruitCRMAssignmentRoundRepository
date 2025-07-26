package com.crm.exception;

import java.util.Date;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.crm.dto.ResponseDto;

import jakarta.validation.ConstraintViolationException;

/**
 * Global exception handler for centralizing error handling across the application.
 * Provides consistent error response structure for different types of exceptions.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handles cases where requested resources are not found in the system.
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResponseDto> handleResourceNotFoundException(
            ResourceNotFoundException exception,
            WebRequest webRequest) {
        LOGGER.error("Resource not found: {}", exception.getMessage());
        return createErrorResponse(
            HttpStatus.NOT_FOUND,
            exception.getMessage(),
            null);
    }

    /**
     * Handles validation constraint violations in request parameters or payload.
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResponseDto> handleValidationExceptions(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations()
            .stream()
            .map(violation -> violation.getMessage())
            .collect(Collectors.joining("; "));
            
        LOGGER.error("Validation failed: {}", errorMessage);
        return createErrorResponse(
            HttpStatus.FAILED_DEPENDENCY,
            errorMessage,
            0);
    }

    /**
     * Handles JPA entity retrieval failures.
     */
    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    public ResponseEntity<ResponseDto> handleJpaObjectRetrievalFailureException(
            JpaObjectRetrievalFailureException ex) {
        String errorMessage = String.format("Database retrieval error: %s", ex.getMessage());
        LOGGER.error(errorMessage);
        return createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            errorMessage,
            0);
    }

    /**
     * Handles unexpected runtime exceptions.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ResponseDto> handleRuntimeExceptions(RuntimeException ex) {
        String errorMessage = String.format("Application error: %s", ex.getMessage());
        LOGGER.error(errorMessage, ex);
        return createErrorResponse(
            HttpStatus.INTERNAL_SERVER_ERROR,
            errorMessage,
            0);
    }

    /**
     * Fallback handler for any unhandled exceptions.
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDto> handleException(Exception ex) {
        LOGGER.error("Unhandled exception occurred", ex);
        return createErrorResponse(
            HttpStatus.BAD_REQUEST,
            ex.getMessage(),
            0);
    }

    /**
     * Creates a standardized error response with the specified status and message.
     */
    private ResponseEntity<ResponseDto> createErrorResponse(HttpStatus status, String message, Object data) {
        ResponseDto responseDto = new ResponseDto(
            false,
            new Date(),
            0,
            message,
            status.toString(),
            data
        );
        return new ResponseEntity<>(responseDto, status);
    }

}
