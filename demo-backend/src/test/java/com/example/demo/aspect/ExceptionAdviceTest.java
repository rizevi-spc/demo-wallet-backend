package com.example.demo.aspect;

import com.example.demo.dto.error.ApiErrorResponse;
import com.example.demo.dto.error.FieldValidationError;
import com.example.demo.exception.CustomValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MissingServletRequestParameterException;

import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExceptionAdviceTest {

    private MessageSource messageSource;
    private ExceptionAdvice advice;

    @BeforeEach
    void setUp() {
        messageSource = mock(MessageSource.class);
        advice = new ExceptionAdvice(messageSource);
    }

    @Test
    void handleBindException_returns400_withFieldErrors() {
        BindException ex = new BindException(this, "target");
        ex.addError(new FieldError("target", "foo", "must not be blank"));

        var resp = advice.handleException(ex);
        assertEquals(400, resp.getStatusCodeValue());

        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertTrue(body.getMessage().startsWith("Validation Error id:"));

        assertEquals(1, ((List<FieldValidationError>)body.getResult()).size());
        FieldValidationError fe = ((List<FieldValidationError>)body.getResult()).get(0);
        assertEquals("foo", fe.field());
        assertEquals("must not be blank", fe.message());
    }

    @Test
    void handleCustomValidationException_returns400_withLocalizedMessage() {
        when(messageSource.getMessage(eq("error.key"), any(), eq(Locale.getDefault())))
                .thenReturn("Localized error");
        CustomValidationException ex = new CustomValidationException("error.key", 42L);

        var resp = advice.handleException(ex);
        assertEquals(400, resp.getStatusCodeValue());

        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertTrue(body.getMessage().startsWith("Custom Validation Error id:"));
        assertEquals("Localized error", body.getResult());
    }

    @Test
    void handleMissingParamException_returns400_withParamName() {
        MissingServletRequestParameterException ex =
                new MissingServletRequestParameterException("foo", "String");

        var resp = advice.handleException(ex);
        assertEquals(400, resp.getStatusCodeValue());

        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertTrue(body.getMessage().startsWith("Missing Request Parameter Error id:"));
        assertEquals("foo", body.getResult());
    }

    @Test
    void handleDataIntegrityViolationException_returns400() {
        var resp = advice.handleException(new DataIntegrityViolationException("dup"));
        assertEquals(400, resp.getStatusCodeValue());

        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(400, body.getStatus());
        assertTrue(body.getMessage().startsWith("Unique contraint exception id:"));
    }

    @Test
    void handleOptimisticLockingFailureException_returns409() {
        var resp = advice.handleException(new OptimisticLockingFailureException("optimistic"));
        assertEquals(409, resp.getStatusCodeValue());

        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(409, body.getStatus());
        assertTrue(body.getMessage().startsWith("Conflict error id:"));
    }

    @Test
    void handleGenericException_returns500() {
        var resp = advice.handleException(new RuntimeException("boom"));
        assertEquals(500, resp.getStatusCodeValue());

        ApiErrorResponse body = resp.getBody();
        assertNotNull(body);
        assertEquals(500, body.getStatus());
        assertTrue(body.getMessage().startsWith("Internal Server Error id:"));
    }

    @Test
    void handleAccessDenied_returns403_withErrorId() {
        var body = advice.handleAccessDenied(new org.springframework.security.access.AccessDeniedException("denied"));
        assertEquals(403, body.getStatus());
        assertNotNull(body.getResult());
        assertTrue(((Map<?, ?>) body.getResult()).containsKey("errorId"));
    }

    @Test
    void handleAuthentication_returns401_withErrorId() {
        var body = advice.handleAuthentication(new org.springframework.security.core.AuthenticationException("unauth") {});
        assertEquals(401, body.getStatus());
        assertNotNull(body.getResult());
        assertTrue(((Map<?, ?>) body.getResult()).containsKey("errorId"));
    }
}
