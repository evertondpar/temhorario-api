package com.temhorario.api.domain.appointment;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record CreateAppointmentDTO(
        @NotNull(message = "O colaborador é obrigatório")
        Long collaboratorId,

        @NotNull(message = "O serviço é obrigatório")
        Long serviceId,

        @NotNull(message = "A data e horário são obrigatórios")
        @Future(message = "O agendamento deve ser para uma data futura")
        LocalDateTime appointmentTime,

        @NotBlank(message = "O nome do cliente é obrigatório")
        String clientName,

        @NotBlank(message = "O telefone do cliente é obrigatório")
        String clientPhone
) {
}
