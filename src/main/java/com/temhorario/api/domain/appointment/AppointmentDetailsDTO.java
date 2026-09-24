package com.temhorario.api.domain.appointment;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record AppointmentDetailsDTO(
        Long id,
        String clientName,
        String collaboratorName,
        String serviceName,
        LocalDateTime appointmentTime,
        BigDecimal price,
        AppointmentStatus status
) {
    public AppointmentDetailsDTO(Appointment appointment) {
        this(
                appointment.getId(),
                appointment.getClient().getName(),
                appointment.getCollaborator().getName(),
                appointment.getService().getName(),
                appointment.getDateTime(),
                appointment.getService().getPrice(),
                appointment.getAppointmentStatus()
        );
    }
}
