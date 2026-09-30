package com.temhorario.api.domain.schedule;

import java.time.DayOfWeek;
import java.util.Map;

public record CompleteScheduleDetailsDTO(
        Map<DayOfWeek, ScheduleDetailsDTO> schedules
) {}