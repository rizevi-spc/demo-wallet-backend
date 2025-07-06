package com.example.demo.aspect;

import com.example.demo.dto.error.ApiErrorResponse;
import com.example.demo.dto.error.FieldValidationError;
import com.example.demo.exception.CustomValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * exception handling advice
 */
@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionAdvice {
    private final MessageSource messageSource;
    static AtomicInteger atom = new AtomicInteger(0);

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiErrorResponse> handleException(BindException ex) {
        long id = generateErrorId();

        ApiErrorResponse apiResponse = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Validation Error id: %d", id))
                .result(ex.getAllErrors()
                        .stream()
                        .filter(FieldError.class::isInstance)
                        .map(FieldError.class::cast)
                        .map(fieldError -> FieldValidationError.of(fieldError.getField(), fieldError.getDefaultMessage()))
                        .collect(Collectors.toList()))
                .build();

        log.error("Error id:{}", id, ex);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleException(CustomValidationException ex) {
        long id = generateErrorId();

        ApiErrorResponse apiResponse = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Custom Validation Error id: %d", id))
                .result(getMessage(ex, Optional.ofNullable(ex.getMethodVal())
                        .orElse(null)))
                .build();

        log.error("Error id:{}", id, ex);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiErrorResponse> handleException(MissingServletRequestParameterException ex) {
        long id = generateErrorId();

        ApiErrorResponse apiResponse = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Missing Request Parameter Error id: %d", id))
                .result(ex.getParameterName())
                .build();

        log.error("Error id:{}", id, ex);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiErrorResponse> handleException(DataIntegrityViolationException ex) {
        long id = generateErrorId();

        ApiErrorResponse apiResponse = ApiErrorResponse.builder().status(HttpStatus.BAD_REQUEST.value())
                .message(String.format("Unique contraint exception id: %d", id))
                .build();

        log.error("Error id:{}", id, ex);
        return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiErrorResponse> handleException(OptimisticLockingFailureException ex) {
        long id = generateErrorId();

        ApiErrorResponse apiResponse = ApiErrorResponse.builder().status(HttpStatus.CONFLICT.value())
                .message(String.format("Conflict error id: %d", id))
                .build();

        log.error("Error id:{}", id, ex);
        return new ResponseEntity<>(apiResponse, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex) {
        long id = generateErrorId();
        ApiErrorResponse apiResponse = ApiErrorResponse.builder().status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message(String.format("Internal Server Error id: %d", id))
                .build();

        log.error("Error id:{}", id, ex);
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiErrorResponse handleAccessDenied(AccessDeniedException ex) {
        return ApiErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(getMessage(ex))
                .result(Map.of("errorId", generateErrorId()))
                .build();
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiErrorResponse handleAuthentication(AuthenticationException ex) {
        return ApiErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message(getMessage(ex))
                .result(Map.of("errorId", generateErrorId()))
                .build();
    }

    private String getMessage(RuntimeException ex, Object... params) {
        return messageSource.getMessage(ex.getMessage(), params, Locale.getDefault());
    }

    /**
     * @return we can seach and find the error in logs with generated id based on timestamp
     */
    private long generateErrorId() {
        return Long.parseLong(System.nanoTime() + "" + (atom.getAndIncrement() % 1_000));
    }

}
