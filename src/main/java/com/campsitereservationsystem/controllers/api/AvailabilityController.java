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
import java.time.format.DateTimeParseException;
import java.util.List;

@Tag(name = "Availability", description = "Campsite Availability APIs")
@RestController
public class AvailabilityController {
    private static final int MAX_AVAILABILITY_DAYS_RANGE = 30;

    @Autowired
    private AvailabilityService availabilityService;

    @GetMapping("/v1/availability")
    public ResponseEntity<List<LocalDate>> computeAvailability(
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(value = "to", required = false) String to
    ) {
        LocalDate rangeStart = getRangeStartOrDefault(parseDate(from));
        LocalDate rangeEnd = getRangeEndOrDefault(parseDate(to), rangeStart);

        if (rangeStart.isBefore(tomorrow())) {
            throw new AvailabilityRequestValidationException("The 'from' date should be no earlier than tomorrow's date");
        }

        Period range = Period.between(rangeStart, rangeEnd);
        if (range.isNegative() || range.isZero()) {
            throw new AvailabilityRequestValidationException("The 'from' date should precede the 'to' date");
        }
        if (range.getDays() > MAX_AVAILABILITY_DAYS_RANGE) {
            throw new AvailabilityRequestValidationException(
                    "The maximum allowed availability search range is " + MAX_AVAILABILITY_DAYS_RANGE + " days"
            );
        }

        List<LocalDate> availableDays = availabilityService.computeAvailableDays(rangeStart, rangeEnd);
        return new ResponseEntity<>(availableDays, HttpStatus.OK);
    }

    private static LocalDate parseDate(String date) {
        try {
            return Strings.isBlank(date) ? null : LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new AvailabilityRequestValidationException("Invalid date string. Could not parse into date.", e);
        }
    }

    private static LocalDate getRangeStartOrDefault(LocalDate rangeStart) {
        return rangeStart == null ? tomorrow() : rangeStart;
    }

    private static LocalDate getRangeEndOrDefault(LocalDate rangeEnd, LocalDate rangeStart) {
        return rangeEnd == null ? rangeStart.plusDays(MAX_AVAILABILITY_DAYS_RANGE) : rangeEnd;
    }

    private static LocalDate tomorrow() {
        return LocalDate.now().plusDays(1);
    }
}