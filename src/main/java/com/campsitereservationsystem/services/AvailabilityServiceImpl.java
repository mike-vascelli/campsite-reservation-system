package com.campsitereservationsystem.services;

import com.campsitereservationsystem.models.ReservedDay;
import com.campsitereservationsystem.repositories.ReservedDayRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AvailabilityServiceImpl implements AvailabilityService {
    private ReservedDayRepository reservedDayRepository;

    @Override
    public List<LocalDate> computeAvailableDays(LocalDate rangeStart, LocalDate rangeEnd) {
        // TODO add cache logic
        Set<LocalDate> reservedDays = reservedDayRepository.findAllByDayGreaterThanEqualAndDayLessThan(
                rangeStart, rangeEnd
        ).stream().map(ReservedDay::getDay).collect(Collectors.toUnmodifiableSet());

        List<LocalDate> availableDays = new ArrayList<>();
        LocalDate day = rangeStart;
        while (day.isBefore(rangeEnd)) {
            if (!reservedDays.contains(day)) {
                availableDays.add(day);
            }
            day = day.plusDays(1);
        }
        return availableDays;
    }
}