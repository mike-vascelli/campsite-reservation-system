package com.campsitereservationsystem.services;

import com.campsitereservationsystem.exceptions.ReservationNotFoundException;
import com.campsitereservationsystem.models.Reservation;
import com.campsitereservationsystem.models.ReservedDay;
import com.campsitereservationsystem.repositories.ReservationRepository;
import com.campsitereservationsystem.repositories.ReservedDayRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class ReservationServiceImpl implements ReservationService {
    private ReservationRepository reservationRepository;
    private ReservedDayRepository reservedDayRepository;

    private static Set<LocalDate> daysInFirstButNotInSecond(Set<LocalDate> first, Set<LocalDate> second) {
        return first.stream().filter(d -> !second.contains(d)).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public Reservation getReservation(UUID reservationId) {
        return reservationRepository.findByIdAndIsCanceledFalse(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException("No reservation exists with id=" + reservationId));
    }

    @Override
    @Transactional
    public Reservation makeReservation(Reservation reservation) {
        Reservation savedReservation = reservationRepository.save(reservation);
        reservedDayRepository.saveAll(ReservedDay.buildFrom(savedReservation));
        return savedReservation;
    }

    @Override
    @Transactional
    public void cancelReservation(UUID reservationId) {
        Reservation reservation = getReservation(reservationId);
        reservation.setIsCanceled(true);
        reservationRepository.save(reservation);
        reservedDayRepository.deleteAllByReservationId(reservation.getId());
    }

    @Override
    @Transactional
    public void updateReservation(UUID reservationId, Reservation updateReservationData) {
        Reservation existingReservation = getReservation(reservationId);
        Reservation updatedReservation = existingReservation.updateWith(updateReservationData);
        if (existingReservation.equals(updatedReservation)) {
            return;
        }

        Set<LocalDate> updateDays = updatedReservation.computeDaysInReservation();
        Set<LocalDate> existingDays = reservedDayRepository.findAllByReservationId(reservationId).stream()
                .map(ReservedDay::getDay).collect(Collectors.toUnmodifiableSet());

        Set<LocalDate> newDaysToCreate = daysInFirstButNotInSecond(updateDays, existingDays);
        if (!newDaysToCreate.isEmpty()) {
            reservedDayRepository.saveAll(ReservedDay.buildFrom(updatedReservation, newDaysToCreate));
        }

        Set<LocalDate> existingDaysToDelete = daysInFirstButNotInSecond(existingDays, updateDays);
        if (!existingDaysToDelete.isEmpty()) {
            reservedDayRepository.deleteAllByDayIn(existingDaysToDelete);
        }

        reservationRepository.save(updatedReservation);
    }

    @Override
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAllByIsCanceledFalse();
    }
}