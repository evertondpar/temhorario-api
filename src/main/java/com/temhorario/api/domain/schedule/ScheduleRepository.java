package com.temhorario.api.domain.schedule;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.DayOfWeek;
import java.util.Optional;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
    Optional<Schedule> findByCollaboratorIdAndDayOfWeek(Long collaboratorId, DayOfWeek dayOfWeek);
}
