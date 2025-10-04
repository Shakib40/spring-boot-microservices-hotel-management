package com.auth.exception;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.auth.dto.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Arrays;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    // Handle validation errors (@NotBlank, @Email, @Size, etc.)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationErrors(MethodArgumentNotValidException ex,
                                                                HttpServletRequest request) {
        // Collect first error message (you can also collect all if needed)
        String message = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + " : " + error.getDefaultMessage())
                .findFirst()
                .orElse("Validation error");

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message(message)
                .path(request.getRequestURI())
                .error("ValidationError")
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    // Handle database constraint violations (duplicate email/username)
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolation(DataIntegrityViolationException ex,
                                                                   HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message("❌ Username or Email already exists")
                .path(request.getRequestURI())
                .error("DataIntegrityViolationException")
                .build();

        return new ResponseEntity<>(response, HttpStatus.CONFLICT); // 409 Conflict
    }


    // Catch-all (generic exception handler)
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                // ✅ only the actual exception message, no prefix
                .message(ex.getMessage())
                .path(request.getRequestURI())
                .error(ex.getClass().getSimpleName())
                .build();

        // You can keep INTERNAL_SERVER_ERROR (500), or map RuntimeException to BAD_REQUEST (400)
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Handle Invalid ENM issue
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidEnum(HttpMessageNotReadableException ex,
                                                           HttpServletRequest request) {
        if (ex.getCause() instanceof InvalidFormatException invalidFormatException &&
                invalidFormatException.getTargetType().isEnum()) {

            // Get allowed enum values dynamically
            String allowedValues = Arrays.stream(invalidFormatException.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));

            ErrorResponse response = ErrorResponse.builder()
                    .success(false)
                    .message("Invalid role value. Allowed values: " + allowedValues)
                    .path(request.getRequestURI())
                    .error("InvalidEnumValue")
                    .build();

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        ErrorResponse response = ErrorResponse.builder()
                .success(false)
                .message("Invalid request payload")
                .path(request.getRequestURI())
                .error("HttpMessageNotReadableException")
                .build();

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    // Handle duplicate resource (like one user = one hotel)
//    @ExceptionHandler(DuplicateResourceException.class)
//    public ResponseEntity<ErrorResponse> handleDuplicateResource(DuplicateResourceException ex,
//                                                                 HttpServletRequest request) {
//        ErrorResponse response = ErrorResponse.builder()
//                .success(false)
//                .message(ex.getMessage()) // comes from service layer
//                .path(request.getRequestURI())
//                .error("DuplicateResourceException")
//                .build();
//
//        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
//    }
}
