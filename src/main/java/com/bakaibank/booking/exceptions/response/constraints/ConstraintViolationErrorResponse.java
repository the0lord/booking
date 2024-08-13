package com.bakaibank.booking.exceptions.response.constraints;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
@Getter
public class ConstraintViolationErrorResponse {
    private final List<Violation> violations;
}
