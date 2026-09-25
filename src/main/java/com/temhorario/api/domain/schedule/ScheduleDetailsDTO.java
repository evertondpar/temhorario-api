package com.temhorario.api.domain.schedule;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record ScheduleDetailsDTO(DayOfWeek dayOfWeek, LocalTime startTime, LocalTime endTime) {
    public ScheduleDetailsDTO(Schedule schedule){
        this(
                schedule.getDayOfWeek(),
                schedule.getStartTime(),
                schedule.getEndTime()
        );
    }
}
