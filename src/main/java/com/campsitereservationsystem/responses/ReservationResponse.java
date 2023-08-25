package com.campsitereservationsystem.responses;

import com.campsitereservationsystem.models.Reservation;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class ReservationResponse {
    private UUID id;

    private LocalDate checkIn;

    private LocalDate checkOut;

    private String email;

    private String firstName;

    private String lastName;

    public static ReservationResponse fromModel(Reservation reservation) {
        return ReservationResponse.builder()
                .id(reservation.getId())
                .email(reservation.getEmail())
                .firstName(reservation.getFirstName())
                .lastName(reservation.getLastName())
                .checkIn(reservation.getCheckIn())
                .checkOut(reservation.getCheckOut())
                .build();
    }
}