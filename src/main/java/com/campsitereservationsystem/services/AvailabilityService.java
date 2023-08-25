package com.campsitereservationsystem.services;

import java.time.LocalDate;
import java.util.List;

public interface AvailabilityService {
    List<LocalDate> computeAvailableDays(LocalDate rangeStart, LocalDate rangeEnd);
}
