package com.nivor.common.exception;

import com.nivor.common.response.ApiError;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> fields = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> fields.putIfAbsent(e.getField(), e.getDefaultMessage()));
        return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", request, fields);
    }
    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> constraint(ConstraintViolationException ex, HttpServletRequest request) { return response(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "Validation failed", request, Map.of("request", "One or more request values are invalid")); }
    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ApiError> notFound(ResourceNotFoundException ex, HttpServletRequest request) { return response(HttpStatus.NOT_FOUND, "NOT_FOUND", ex.getMessage(), request, Map.of()); }
    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ApiError> badRequest(IllegalArgumentException ex, HttpServletRequest request) { return response(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), request, Map.of()); }
    @ExceptionHandler(DataIntegrityViolationException.class)
    ResponseEntity<ApiError> conflict(DataIntegrityViolationException ex, HttpServletRequest request) { return response(HttpStatus.CONFLICT, "CONFLICT", "The request conflicts with existing data", request, Map.of()); }
    @ExceptionHandler(AccessDeniedException.class)
    ResponseEntity<ApiError> forbidden(AccessDeniedException ex, HttpServletRequest request) { return response(HttpStatus.FORBIDDEN, "FORBIDDEN", "Access denied", request, Map.of()); }
    @ExceptionHandler(BadCredentialsException.class)
    ResponseEntity<ApiError> unauthorized(BadCredentialsException ex, HttpServletRequest request) { return response(HttpStatus.UNAUTHORIZED, "INVALID_CREDENTIALS", "Invalid email or password", request, Map.of()); }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    ResponseEntity<ApiError> mismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest request) { return response(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "A request parameter has an invalid value", request, Map.of()); }
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    ResponseEntity<ApiError> uploadTooLarge(MaxUploadSizeExceededException ex, HttpServletRequest request) { return response(HttpStatus.PAYLOAD_TOO_LARGE, "FILE_TOO_LARGE", "Uploaded file exceeds the 10 MB limit", request, Map.of()); }
    @ExceptionHandler(Exception.class)
    ResponseEntity<ApiError> unexpected(Exception ex, HttpServletRequest request) { return response(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "An unexpected error occurred", request, Map.of()); }
    private ResponseEntity<ApiError> response(HttpStatus status, String error, String message, HttpServletRequest request, Map<String, String> fields) {
        return ResponseEntity.status(status).body(new ApiError(Instant.now(), status.value(), error, message, request.getRequestURI(), fields));
    }
}
