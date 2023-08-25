package com.campsitereservationsystem.exceptions;

public class AvailabilityRequestValidationException extends RuntimeException {
    public AvailabilityRequestValidationException(String message) {
        super(message);
    }

    public AvailabilityRequestValidationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
