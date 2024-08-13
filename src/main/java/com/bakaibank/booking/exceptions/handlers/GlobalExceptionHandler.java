package com.bakaibank.booking.exceptions.handlers;

import com.bakaibank.booking.exceptions.AlreadyAuthenticatedException;
import com.bakaibank.booking.exceptions.RelatedEntityExistsException;
import com.bakaibank.booking.exceptions.ValidationException;
import com.bakaibank.booking.exceptions.response.Error;
import com.bakaibank.booking.exceptions.response.ValidationErrorResponse;
import com.bakaibank.booking.exceptions.response.constraints.ConstraintViolationErrorResponse;
import com.bakaibank.booking.exceptions.response.constraints.Violation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ConstraintViolationErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        List<Violation> violations = e.getConstraintViolations().stream()
                .map(
                        violation -> new Violation(
                                violation.getPropertyPath().toString(),
                                violation.getMessage()
                        )
                )
                .toList();

        return new ConstraintViolationErrorResponse(violations);
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleValidationException(ValidationException e) {
        List<Error> errors = e.getErrors().getAllErrors().stream()
                .map(
                        error -> new Error(
                                error.getCode(),
                                error.getDefaultMessage())
                )
                .toList();

        return new ValidationErrorResponse(errors);
    }

    @ExceptionHandler(RelatedEntityExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Error handleRelatedEntityExistsException(RelatedEntityExistsException e) {
        return new Error("relatedEntityExists", e.getMessage());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleBadCredentialsException(BadCredentialsException e) {
        return new Error("badCredentials", e.getMessage());
    }

    @ExceptionHandler(AlreadyAuthenticatedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Error handleAlreadyAuthenticatedException(AlreadyAuthenticatedException e) {
        return new Error("alreadyAuthenticated", e.getMessage());
    }
}
