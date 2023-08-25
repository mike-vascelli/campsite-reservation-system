package com.campsitereservationsystem.controllers.api;

import com.campsitereservationsystem.exceptions.AvailabilityRequestValidationException;
import com.campsitereservationsystem.services.AvailabilityService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Tag(name = "Availability", description = "Campsite Availability APIs")
@RestController
public class AvailabilityController {
    private static final int DEFAULT_RANGE_START_DAY_INCREMENT = 1;
    private static final int DEFAULT_RANGE_END_DAY_INCREMENT = 30;

    @Autowired
    private AvailabilityService availabilityService;

    private static LocalDate parseDay(String date, int default_day_increment) {
        try {
            return Strings.isBlank(date) ? LocalDate.now().plusDays(default_day_increment) : LocalDate.parse(date);
        } catch (Exception e) {
            throw new AvailabilityRequestValidationException("Invalid date string. Could not parse into date.", e);
        }
    }

    @GetMapping("/v1/availability")
    public ResponseEntity<List<LocalDate>> computeAvailability(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        LocalDate rangeStart = parseDay(from, DEFAULT_RANGE_START_DAY_INCREMENT);
        LocalDate rangeEnd = parseDay(to, DEFAULT_RANGE_END_DAY_INCREMENT);

        if (!rangeStart.isBefore(rangeEnd)) {
            throw new AvailabilityRequestValidationException("The 'from' date should precede the 'to' date");
        }
        if (Period.between(rangeStart, rangeEnd).getDays() > DEFAULT_RANGE_END_DAY_INCREMENT) {
            throw new AvailabilityRequestValidationException(
                    "The maximum allowed availability search range is " + DEFAULT_RANGE_END_DAY_INCREMENT + " days"
            );
        }

        List<LocalDate> availableDays = availabilityService.computeAvailableDays(rangeStart, rangeEnd);
        return new ResponseEntity<>(availableDays, HttpStatus.OK);
    }
}