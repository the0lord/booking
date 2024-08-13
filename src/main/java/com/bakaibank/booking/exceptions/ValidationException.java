package com.bakaibank.booking.exceptions;

import lombok.Getter;
import org.springframework.validation.Errors;

@Getter
public class ValidationException extends RuntimeException {
    private final Errors errors;

    public ValidationException(Errors errors) {
        this.errors = errors;
    }
}
