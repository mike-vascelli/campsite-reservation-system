package com.campsitereservationsystem.services;

import com.campsitereservationsystem.models.Reservation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

public interface ReservationService {
    Reservation getReservation(UUID reservationId);

    @Transactional
    Reservation makeReservation(Reservation reservation);

    @Transactional
    void cancelReservation(UUID reservationId);

    @Transactional
    void updateReservation(UUID reservationId, Reservation updateReservationData);

    List<Reservation> getAllReservations();
}
