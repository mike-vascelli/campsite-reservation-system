package com.campsitereservationsystem.validators.annotations;

import com.campsitereservationsystem.validators.CheckReservationRangeValidator;
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
@Constraint(validatedBy = {CheckReservationRangeValidator.class})
@Documented
public @interface CheckReservationRange {
    String message() default "The checkIn date should come before the checkOut date";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
