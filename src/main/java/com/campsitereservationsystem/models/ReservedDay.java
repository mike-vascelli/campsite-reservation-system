package com.campsitereservationsystem.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.SourceType;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Entity
@Table(
        name = "reserved_days",
        indexes = {
                @Index(name = "day_index", columnList = "day", unique = true),
                @Index(name = "reservation_id_index", columnList = "reservation_id"),
        }
)
public class ReservedDay {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(nullable = false)
    private LocalDate day;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "reservation_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Reservation reservation;

    @Column(nullable = false)
    @CreationTimestamp(source = SourceType.DB)
    private Instant createdAt;

    public static List<ReservedDay> buildFrom(Reservation reservation, Set<LocalDate> reservedDays) {
        return reservedDays.stream()
                .map(day -> ReservedDay.builder().day(day).reservation(reservation).build()).toList();
    }

    public static List<ReservedDay> buildFrom(Reservation reservation) {
        return reservation.computeDaysInReservation().stream()
                .map(day -> ReservedDay.builder().day(day).reservation(reservation).build()).toList();
    }
}