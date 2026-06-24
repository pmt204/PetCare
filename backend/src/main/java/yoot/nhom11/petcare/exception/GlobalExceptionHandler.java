package yoot.nhom11.petcare.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.validation.ConstraintViolationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        ErrorResponse response = ErrorResponse.builder()
                .code(errorCode.getCode())
                .message(ex.getMessage())
                .status(errorCode.getStatus().value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .details(ex.getDetails())
                .build();
        return new ResponseEntity<>(response, errorCode.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, String> details = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            if (error instanceof FieldError fieldError) {
                details.put(fieldError.getField(), error.getDefaultMessage());
            } else {
                details.put(error.getObjectName(), error.getDefaultMessage());
            }
        });

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.VALIDATION_FAILED.getCode())
                .message(ErrorCode.VALIDATION_FAILED.getDefaultMessage())
                .status(ErrorCode.VALIDATION_FAILED.getStatus().value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();
        return new ResponseEntity<>(response, ErrorCode.VALIDATION_FAILED.getStatus());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> details = new HashMap<>();
        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String paramName = propertyPath;
            int lastDot = propertyPath.lastIndexOf('.');
            if (lastDot != -1) {
                paramName = propertyPath.substring(lastDot + 1);
            }
            details.put(paramName, violation.getMessage());
        });

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.VALIDATION_FAILED.getCode())
                .message(ErrorCode.VALIDATION_FAILED.getDefaultMessage())
                .status(ErrorCode.VALIDATION_FAILED.getStatus().value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .details(details)
                .build();
        return new ResponseEntity<>(response, ErrorCode.VALIDATION_FAILED.getStatus());
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ErrorResponse> handleResponseStatusException(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.resolve(ex.getStatusCode().value());
        if (status == null) {
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        ErrorResponse response = ErrorResponse.builder()
                .code("LEGACY_ERROR")
                .message(ex.getReason() != null ? ex.getReason() : ex.getMessage())
                .status(status.value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled exception occurred on path: {}", request.getRequestURI(), ex);

        ErrorResponse response = ErrorResponse.builder()
                .code(ErrorCode.INTERNAL_ERROR.getCode())
                .message(ErrorCode.INTERNAL_ERROR.getDefaultMessage())
                .status(ErrorCode.INTERNAL_ERROR.getStatus().value())
                .path(request.getRequestURI())
                .timestamp(LocalDateTime.now())
                .build();
        return new ResponseEntity<>(response, ErrorCode.INTERNAL_ERROR.getStatus());
    }
}
