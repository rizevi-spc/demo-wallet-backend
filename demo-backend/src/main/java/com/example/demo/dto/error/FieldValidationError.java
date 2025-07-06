package com.example.demo.dto.error;

import java.io.Serializable;

/**
 * type for field validation error
 * @param field dto field name
 * @param message  error message
 */
public record FieldValidationError(String field, String message) implements Serializable {
    public static FieldValidationError of(String field, String message) {
        return new FieldValidationError(field, message);
    }
}