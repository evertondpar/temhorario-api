package com.temhorario.api.domain.appointment;

import com.temhorario.api.domain.client.Client;
import com.temhorario.api.domain.client.ClientRepository;
import com.temhorario.api.domain.collaborator.CollaboratorRepository;
import com.temhorario.api.domain.schedule.Schedule;
import com.temhorario.api.domain.schedule.ScheduleRepository;
import com.temhorario.api.domain.service.ServiceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class AppointmentService {
    private final AppointmentRepository repository;
    private final CollaboratorRepository collaboratorRepository;
    private final ServiceRepository serviceRepository;
    private final ClientRepository clientRepository;
    private final ScheduleRepository scheduleRepository;

    public AppointmentService(
            AppointmentRepository repository,
            CollaboratorRepository collaboratorRepository,
            ServiceRepository serviceRepository,
            ClientRepository clientRepository,
            ScheduleRepository scheduleRepository
    ) {
        this.repository = repository;
        this.collaboratorRepository = collaboratorRepository;
        this.serviceRepository = serviceRepository;
        this.clientRepository = clientRepository;
        this.scheduleRepository = scheduleRepository;
    }

    //cria agendamento
    @Transactional
    public AppointmentDetailsDTO createAppointment(CreateAppointmentDTO data){
        var collaborator = collaboratorRepository.findById(data.collaboratorId())
                .orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));
        var service = serviceRepository.findById(data.serviceId())
                .orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        Client client = clientRepository.findByPhone(data.clientPhone())
                .orElseGet(() -> {
                    Client newClient = new Client();
                    newClient.setName(data.clientName());
                    newClient.setPhone(data.clientPhone());
                    return clientRepository.save(newClient);
                });

        Appointment appointment = new Appointment(client, collaborator, service, data.appointmentTime(), AppointmentStatus.SCHEDULED);
        boolean isOcupado = repository.existsByCollaboratorIdAndDateTime(data.collaboratorId(), data.appointmentTime());
        if (isOcupado) {
            throw new RuntimeException("Este horário já está reservado.");
        }
        appointment = repository.save(appointment);
        return new AppointmentDetailsDTO(appointment);
    }

    //consulta horário disponíveis
    @Transactional(readOnly = true)
    public AvailableTimesDto findAvailableTimes(Long collaboratorId, Long serviceId, LocalDate date){
        var service = serviceRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("Serviço não encontrado"));

        var dayOfWeek = date.getDayOfWeek();
        //pega a agenda do collaborador no dia específico da semana
        Schedule schedule = scheduleRepository.findByCollaboratorIdAndDayOfWeek(collaboratorId, dayOfWeek).orElse(null);
        if(schedule == null){
            return new AvailableTimesDto(date, List.of());
        }

        //busca agendamentos na data específica
        LocalDateTime startOfDay = date.atStartOfDay();
        LocalDateTime endOfDay = date.atTime(LocalTime.MAX);
        List<Appointment> existingAppointments = repository.findAllByCollaboratorIdAndDateTimeBetween(collaboratorId, startOfDay, endOfDay);

        //extrai horários ocupados
        List<LocalTime> bookedTimes = existingAppointments.stream()
                .map(app -> app.getDateTime().toLocalTime())
                .toList();

        //gera grade de horários disponíveis incrementando pela duração do serviço
        List<LocalTime> availableTimes = new ArrayList<>();
        LocalTime currentTime = schedule.getStartTime();
        int serviceDuration = service.getDurationInMinutes();

        while(!currentTime.plusMinutes(serviceDuration).isAfter(schedule.getEndTime())){
            LocalTime slotStart = currentTime;
            LocalTime slotEnd = currentTime.plusMinutes(serviceDuration);

            // Checa se o slotStart ou slotEnd bate com algum agendamento
            boolean hasConflict = existingAppointments.stream().anyMatch(appointment -> {
                LocalTime appStart = appointment.getStartTime();
                LocalTime appEnd = appointment.getEndTime();

                // Verifica colisão se slotStart < appEnd E slotEnd > appStart
                return slotStart.isBefore(appEnd) && slotEnd.isAfter(appStart);
            });

            if (!hasConflict) {
                availableTimes.add(currentTime);
            }

            currentTime = currentTime.plusMinutes(30);
        }

        return new AvailableTimesDto(date, availableTimes);
    }
}
