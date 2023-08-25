package com.campsitereservationsystem.validators;

import com.campsitereservationsystem.requests.ReservationRequest;
import com.campsitereservationsystem.validators.annotations.CheckAdvanceReservation;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class CheckAdvanceReservationValidator implements ConstraintValidator<CheckAdvanceReservation, ReservationRequest> {
    @Override
    public void initialize(CheckAdvanceReservation constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ReservationRequest reservationRequest, ConstraintValidatorContext context) {
        LocalDate checkIn = reservationRequest.getCheckIn();
        if (checkIn == null) {
            return true;
        }
        LocalDate latest_reservable_date = LocalDate.now().plusDays(CheckAdvanceReservation.MAX_ADVANCE_RESERVATION_DAYS);
        return checkIn.isBefore(latest_reservable_date);
    }
}
