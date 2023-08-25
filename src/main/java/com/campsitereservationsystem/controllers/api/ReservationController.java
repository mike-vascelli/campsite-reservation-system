package com.campsitereservationsystem.controllers.api;

import com.campsitereservationsystem.models.Reservation;
import com.campsitereservationsystem.requests.ReservationRequest;
import com.campsitereservationsystem.responses.ReservationResponse;
import com.campsitereservationsystem.services.ReservationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Tag(name = "Reservations", description = "Campsite Reservations APIs")
@RestController
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping("/v1/reservations")
    public ResponseEntity<ReservationResponse> makeReservation(@Valid @RequestBody ReservationRequest reservationRequest) {
        Reservation reservation = reservationService.makeReservation(reservationRequest.toModel());
        return new ResponseEntity<>(ReservationResponse.fromModel(reservation), HttpStatus.CREATED);
    }

    @PutMapping("/v1/reservations/{id}")
    public ResponseEntity<HttpStatus> updateReservation(
            @PathVariable UUID id, @Valid @RequestBody ReservationRequest reservationRequest
    ) {
        reservationService.updateReservation(id, reservationRequest.toModel());
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/v1/reservations/{id}")
    public ResponseEntity<HttpStatus> cancelReservation(@PathVariable UUID id) {
        reservationService.cancelReservation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/v1/reservations/{id}")
    public ResponseEntity<ReservationResponse> getReservation(@PathVariable UUID id) {
        Reservation reservation = reservationService.getReservation(id);
        return new ResponseEntity<>(ReservationResponse.fromModel(reservation), HttpStatus.OK);
    }

    @GetMapping("/v1/reservations")
    public ResponseEntity<List<ReservationResponse>> getAllReservations() {
        return new ResponseEntity<>(
                reservationService.getAllReservations().stream().map(ReservationResponse::fromModel)
                        .collect(Collectors.toList()),
                HttpStatus.OK
        );
    }
}