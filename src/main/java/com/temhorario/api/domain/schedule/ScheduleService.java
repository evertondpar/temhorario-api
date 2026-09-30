package com.temhorario.api.domain.schedule;

import com.temhorario.api.domain.collaborator.Collaborator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ScheduleService {
    private final ScheduleRepository repository;

    public ScheduleService(ScheduleRepository repository){
        this.repository = repository;
    }

    //criar 7 dias da semana da agenda do colaborador
    @Transactional
    public CompleteScheduleDetailsDTO createCompleteSchedule(Collaborator collaborator){

        List<Schedule> schedulesToSave = Arrays.stream(DayOfWeek.values())
                .map(day -> {
                    Schedule schedule = new Schedule();
                    schedule.setCollaborator(collaborator);
                    schedule.setDayOfWeek(day);
                    schedule.setActive(true);
                    schedule.setStartTime(LocalTime.of(8,0));
                    schedule.setEndTime(LocalTime.of(18,0));
                    return schedule;
                })
                .toList();

        List<Schedule> savedSchedules = repository.saveAll(schedulesToSave);

        Map<DayOfWeek, ScheduleDetailsDTO> scheduleMap = savedSchedules.stream()
                .collect(Collectors.toMap(
                        Schedule::getDayOfWeek,
                        ScheduleDetailsDTO::new,
                        (existing, replacement) -> existing,
                        () -> new EnumMap<>(DayOfWeek.class)
                ));

        return new CompleteScheduleDetailsDTO(scheduleMap);
    }
}
