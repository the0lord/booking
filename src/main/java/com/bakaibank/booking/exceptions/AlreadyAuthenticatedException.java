package com.bakaibank.booking.exceptions;

public class AlreadyAuthenticatedException extends RuntimeException {
    public AlreadyAuthenticatedException() {
    }

    public AlreadyAuthenticatedException(String message) {
        super(message);
    }
}
