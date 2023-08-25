package com.campsitereservationsystem.validators;

import com.campsitereservationsystem.requests.ReservationRequest;
import com.campsitereservationsystem.validators.annotations.CheckReservationRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class CheckReservationRangeValidator implements ConstraintValidator<CheckReservationRange, ReservationRequest> {

    @Override
    public void initialize(CheckReservationRange constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ReservationRequest reservationRequest, ConstraintValidatorContext context) {
        LocalDate checkIn = reservationRequest.getCheckIn();
        LocalDate checkOut = reservationRequest.getCheckOut();
        if (checkIn == null || checkOut == null) {
            return true;
        }

        return checkOut.isAfter(checkIn);
    }
}
