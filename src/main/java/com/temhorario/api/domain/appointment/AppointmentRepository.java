package com.temhorario.api.domain.appointment;

import com.temhorario.api.domain.schedule.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findAllByCollaboratorIdAndDateTimeBetween(Long collaboratorId, LocalDateTime startOfDay, LocalDateTime endOfDay);

}
