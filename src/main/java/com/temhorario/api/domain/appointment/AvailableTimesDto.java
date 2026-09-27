package com.temhorario.api.domain.appointment;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record AvailableTimesDto(LocalDate date, List<LocalTime> availableTimes) {
}
