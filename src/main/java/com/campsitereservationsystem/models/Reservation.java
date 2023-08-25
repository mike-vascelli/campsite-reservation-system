package com.campsitereservationsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(
        name = "reservations",
        indexes = {@Index(name = "is_canceled_index", columnList = "isCanceled")}
)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private Boolean isCanceled;

    @Column(nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;

    @OneToMany(mappedBy = "reservation", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ReservedDay> reservedDays;

    public Reservation updateWith(Reservation reservationData) {
        return Reservation.builder()
                .id(getId())
                .createdAt(getCreatedAt())
                .isCanceled(getIsCanceled())
                .checkIn(reservationData.getCheckIn())
                .checkOut(reservationData.getCheckOut())
                .email(reservationData.getEmail())
                .firstName(reservationData.getFirstName())
                .lastName(reservationData.getLastName())
                .build();
    }

    public Set<LocalDate> computeDaysInReservation() {
        Set<LocalDate> days = new HashSet<>();
        LocalDate d = getCheckIn();
        while (d.isBefore(getCheckOut())) {
            days.add(d);
            d = d.plusDays(1);
        }
        return days;
    }
}