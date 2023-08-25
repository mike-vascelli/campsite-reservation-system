package com.campsitereservationsystem.exceptions;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@Getter
enum ErrorCodes {

    RESERVATION_NOT_FOUND(1),
    CANNOT_CREATE_RESERVATION_FOR_GIVEN_DAYS(2),
    FAILED_RESERVATION_REQUEST_VALIDATION(3),
    FAILED_AVAILABILITY_REQUEST_VALIDATION(4);

    private final int code;

    ErrorCodes(int code) {
        this.code = code;
    }
}

@Slf4j
@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler
    public ResponseEntity<?> reservationNotFound(ReservationNotFoundException ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(new ResponseStatusError(ErrorCodes.RESERVATION_NOT_FOUND, ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> availabilityRequestException(AvailabilityRequestValidationException ex) {
        log.warn(ex.getMessage(), ex);
        return ResponseEntity.badRequest()
                .body(new ResponseStatusError(ErrorCodes.FAILED_AVAILABILITY_REQUEST_VALIDATION, ex.getMessage()));
    }


    @ExceptionHandler
    public ResponseEntity<?> duplicateReservationException(DataIntegrityViolationException ex) {
        log.error(ex.getMessage(), ex);
        return ResponseEntity.unprocessableEntity()
                .body(new ResponseStatusError(ErrorCodes.CANNOT_CREATE_RESERVATION_FOR_GIVEN_DAYS, ex.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<?> requestValidationExceptions(MethodArgumentNotValidException ex) {
        log.warn(ex.getMessage(), ex);
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
            errors.put(fieldName, error.getDefaultMessage());
        });
        return ResponseEntity.badRequest()
                .body(new ResponseStatusError(ErrorCodes.FAILED_RESERVATION_REQUEST_VALIDATION, errors));
    }
}

record ResponseStatusError(String message, Object errorDetails, int code) {
    public ResponseStatusError(ErrorCodes error, Object errorDetails) {
        this(error.name(), errorDetails, error.getCode());
    }
}