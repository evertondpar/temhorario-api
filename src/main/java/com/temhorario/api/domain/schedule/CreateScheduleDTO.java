package com.temhorario.api.domain.schedule;

import jakarta.validation.constraints.NotNull;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record CreateScheduleDTO(
        @NotNull(message = "O dia da semana é obrigatório")
        DayOfWeek dayOfWeek,

        @NotNull(message = "O horário inicial é obrigatório")
        LocalTime startTime,

        @NotNull(message = "O horário final é obrigatório")
        LocalTime endTime
) {
}
