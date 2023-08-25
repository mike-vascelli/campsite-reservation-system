package com.campsitereservationsystem.repositories;

import com.campsitereservationsystem.models.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Optional<Reservation> findByIdAndIsCanceledFalse(UUID id);

    List<Reservation> findAllByIsCanceledFalse();
}