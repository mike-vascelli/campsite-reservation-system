package com.campsitereservationsystem.validators.annotations;

import com.campsitereservationsystem.validators.CheckAdvanceReservationValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;


@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {CheckAdvanceReservationValidator.class})
@Documented
public @interface CheckAdvanceReservation {
    int MAX_ADVANCE_RESERVATION_DAYS = 30;

    String message() default "Cannot make a reservation for more than " + MAX_ADVANCE_RESERVATION_DAYS + "days in advance";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

