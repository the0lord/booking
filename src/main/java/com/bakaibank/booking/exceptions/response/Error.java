package com.bakaibank.booking.exceptions.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Error {
    private final String code;
    private final String errorMessage;
}
