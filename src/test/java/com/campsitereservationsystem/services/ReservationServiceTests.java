package com.campsitereservationsystem.services;

import com.campsitereservationsystem.models.Reservation;
import com.campsitereservationsystem.models.ReservedDay;
import com.campsitereservationsystem.repositories.ReservationRepository;
import com.campsitereservationsystem.repositories.ReservedDayRepository;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;


import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@SpringBootTest
class ReservationServiceTests {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ReservedDayRepository reservedDayRepository;

    @Autowired
    private ReservationService reservationService;

    @BeforeEach
    public void setUp() {
        reservedDayRepository.deleteAll();
        reservationRepository.deleteAll();
    }

    @Test
    void testGetReservation() {
        Reservation reservation = buildReservationRecord(1);
        reservationRepository.save(reservation);

        // When
        Reservation actualReservation = reservationService.getReservation(reservation.getId());

        // Then
        assertOnReservation(reservation, actualReservation);
        assertEquals(reservation.getId(), actualReservation.getId());
    }

    @Test
    void testCreateReservation() {
        Reservation reservation = buildReservationRecord(1);

        // When
        Reservation actualReservation = reservationService.makeReservation(reservation);

        // Then
        assertOnReservation(reservation, actualReservation);
        List<ReservedDay> reservedDays = reservedDayRepository.findAllByReservationId(actualReservation.getId());
        assertEquals(1, reservedDays.size());
        ReservedDay reservedDay = reservedDays.get(0);
        assertEquals(reservation.getCheckIn(), reservedDay.getDay());
        assertEquals(reservation.getId(), reservedDay.getReservation().getId());
        assertNotNull(reservation.getCreatedAt());
    }

    @Test
    void testCreateReservationWithMultipleReservationsWithOverlappingDates() {
        Reservation reservation = buildReservationRecord(2);

        // When
        Reservation actualReservation = reservationService.makeReservation(reservation);

        // Then
        assertOnReservation(reservation, actualReservation);
        List<ReservedDay> reservedDays = reservedDayRepository.findAllByReservationId(actualReservation.getId());
        assertEquals(2, reservedDays.size());

        Set<LocalDate> expectedDays = Set.of(LocalDate.now(), LocalDate.now().plusDays(1));
        assertEquals(expectedDays, reservedDays.stream().map(ReservedDay::getDay).collect(Collectors.toUnmodifiableSet()));

        // When
        Reservation longerReservation = buildReservationRecord(3);

        // Then
        assertThrows(DataIntegrityViolationException.class, () -> reservationService.makeReservation(longerReservation));
    }

    private void assertOnReservation(Reservation expected, Reservation actual) {
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getFirstName(), actual.getFirstName());
        assertEquals(expected.getLastName(), actual.getLastName());
        assertEquals(expected.getCheckIn(), actual.getCheckIn());
        assertEquals(expected.getCheckOut(), actual.getCheckOut());
        assertEquals(expected.getIsCanceled(), actual.getIsCanceled());
        assertNotNull(actual.getCreatedAt());
    }

    public Reservation buildReservationRecord(int days) {
        return Reservation.builder()
                .email("who@that.com")
                .firstName("who")
                .lastName("that")
                .checkIn(LocalDate.now())
                .checkOut(LocalDate.now().plusDays(days))
                .isCanceled(false)
                .build();
    }

}
