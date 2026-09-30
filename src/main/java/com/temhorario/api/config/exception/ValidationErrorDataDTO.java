package com.temhorario.api.config.exception;

import org.springframework.validation.FieldError;

public record ValidationErrorDataDTO(String field, String message) {
    public ValidationErrorDataDTO(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}
