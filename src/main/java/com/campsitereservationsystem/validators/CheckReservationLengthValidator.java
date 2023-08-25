package com.campsitereservationsystem.validators;

import com.campsitereservationsystem.requests.ReservationRequest;
import com.campsitereservationsystem.validators.annotations.CheckReservationLength;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;

public class CheckReservationLengthValidator implements ConstraintValidator<CheckReservationLength, ReservationRequest> {

    @Override
    public void initialize(CheckReservationLength constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(ReservationRequest reservationRequest, ConstraintValidatorContext context) {
        LocalDate checkIn = reservationRequest.getCheckIn();
        LocalDate checkOut = reservationRequest.getCheckOut();
        if (checkIn == null || checkOut == null) {
            return true;
        }

        if (!checkOut.isAfter(checkIn)) {
            // This condition is checked by CheckReservationRange
            return true;
        }

        LocalDate latest_possible_reservation_day = checkIn.plusDays(CheckReservationLength.MAX_RESERVATION_LENGTH);
        return !checkOut.isAfter(latest_possible_reservation_day);
    }
}
