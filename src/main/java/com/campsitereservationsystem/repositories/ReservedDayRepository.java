package com.campsitereservationsystem.repositories;

import com.campsitereservationsystem.models.ReservedDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ReservedDayRepository extends JpaRepository<ReservedDay, Integer> {
    void deleteAllByReservationId(UUID reservationId);

    void deleteAllByDayIn(Set<LocalDate> days);

    List<ReservedDay> findAllByReservationId(UUID reservationId);

    List<ReservedDay> findAllByDayGreaterThanEqualAndDayLessThan(LocalDate rangeStart, LocalDate rangeEnd);
}