package com.campsitereservationsystem.requests;

import com.campsitereservationsystem.models.Reservation;
import com.campsitereservationsystem.validators.annotations.CheckAdvanceReservation;
import com.campsitereservationsystem.validators.annotations.CheckReservationLength;
import com.campsitereservationsystem.validators.annotations.CheckReservationRange;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@CheckReservationLength
@CheckReservationRange
@CheckAdvanceReservation
public class ReservationRequest {

    @Future(message = "The 'checkIn' date must be in the future")
    @NotNull(message = "The 'checkIn' date cannot be blank")
    private LocalDate checkIn;

    @Future(message = "The 'checkOut' date must be in the future")
    @NotNull(message = "The 'checkOut' date cannot be blank")
    private LocalDate checkOut;

    @Email(message = "The email must be valid", flags = {Pattern.Flag.CASE_INSENSITIVE})
    @NotBlank(message = "The 'email' cannot be blank")
    private String email;

    @NotBlank(message = "The 'firstName' cannot be blank")
    private String firstName;

    @NotBlank(message = "The 'lastName' cannot be blank")
    private String lastName;

    public Reservation toModel() {
        return Reservation.builder()
                .email(getEmail())
                .firstName(getFirstName())
                .lastName(getLastName())
                .checkIn(getCheckIn())
                .checkOut(getCheckOut())
                .isCanceled(false)
                .build();
    }
}