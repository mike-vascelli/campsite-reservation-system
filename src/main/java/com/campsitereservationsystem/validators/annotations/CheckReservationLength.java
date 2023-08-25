package com.campsitereservationsystem.validators.annotations;

import com.campsitereservationsystem.validators.CheckReservationLengthValidator;
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
@Constraint(validatedBy = {CheckReservationLengthValidator.class})
@Documented
public @interface CheckReservationLength {
    int MAX_RESERVATION_LENGTH = 3;

    String message() default "A reservation cannot be longer than " + MAX_RESERVATION_LENGTH + " days";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
