package com.bakaibank.booking.exceptions;

public class RelatedEntityExistsException extends RuntimeException {
    public RelatedEntityExistsException() {
    }

    public RelatedEntityExistsException(String message) {
        super(message);
    }
}
