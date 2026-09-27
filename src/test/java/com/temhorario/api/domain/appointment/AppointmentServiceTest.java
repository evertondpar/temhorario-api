package com.temhorario.api.domain.appointment;


import com.temhorario.api.domain.client.Client;
import com.temhorario.api.domain.client.ClientRepository;
import com.temhorario.api.domain.collaborator.Collaborator;
import com.temhorario.api.domain.collaborator.CollaboratorRepository;
import com.temhorario.api.domain.schedule.Schedule;
import com.temhorario.api.domain.schedule.ScheduleRepository;
import com.temhorario.api.domain.service.Service;
import com.temhorario.api.domain.service.ServiceRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppointmentServiceTest {

    @Mock
    private AppointmentRepository repository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Mock
    private CollaboratorRepository collaboratorRepository;

    @Mock
    private ClientRepository clientRepository;

    @InjectMocks
    private AppointmentService appointmentService;

    @Test
    @DisplayName("Deve retornar horários disponíveis quando não houver agendamentos no dia")
    void shouldReturnAvailableTimesWhenNoAppointmentsExist() {
        //prepara mocks
        Long collaboratorId = 1L;
        Long serviceId = 1L;
        LocalDate date = LocalDate.of(2026, 9, 28);

        //serviço fake
        Service mockService = new Service();
        mockService.setDurationInMinutes(30);

        //agenda fake (8:00 às 9:00)
        Schedule mockSchedule = new Schedule();
        mockSchedule.setStartTime(LocalTime.of(8,0));
        mockSchedule.setEndTime(LocalTime.of(9,0));

        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(mockService));
        when(scheduleRepository.findByCollaboratorIdAndDayOfWeek(collaboratorId, DayOfWeek.MONDAY))
                .thenReturn(Optional.of(mockSchedule));
        when(repository.findAllByCollaboratorIdAndDateTimeBetween(eq(collaboratorId), any(), any()))
                .thenReturn(List.of());

        // 2. ACT (Execução do método real)
        AvailableTimesDto result = appointmentService.findAvailableTimes(collaboratorId, serviceId, date);

        // 3. ASSERT (Validação dos resultados)
        assertNotNull(result);
        assertEquals(2, result.availableTimes().size()); // Esperamos 2 slots: 08:00 e 08:30
        assertTrue(result.availableTimes().contains(LocalTime.of(8, 0)));
        assertTrue(result.availableTimes().contains(LocalTime.of(8, 30)));
    }

    @Test
    @DisplayName("Não deve retornar horários disponíveis se a duração do serviço for maior do que os slots disponíveis")
    void shouldNotReturnAvailableTimesWhenServiceDurationIsHigher(){

        Long collaboratorId = 1L;
        Long serviceId = 1L;
        LocalDate date = LocalDate.of(2026, 9, 28);

        Service mockService = new Service();
        mockService.setDurationInMinutes(120);

        Schedule mockSchedule = new Schedule();
        mockSchedule.setStartTime(LocalTime.of(8, 0));
        mockSchedule.setEndTime(LocalTime.of(10, 0));

        Service existingAppointmentService = new Service();
        existingAppointmentService.setDurationInMinutes(30);

        Appointment mockAppointment = new Appointment();
        mockAppointment.setDateTime((LocalDateTime.of(2026, 9, 28, 9, 30, 0)));
        mockAppointment.setService(existingAppointmentService);


        when(serviceRepository.findById(serviceId)).
                thenReturn(Optional.of(mockService));

        when(scheduleRepository.findByCollaboratorIdAndDayOfWeek(collaboratorId, DayOfWeek.MONDAY))
                .thenReturn(Optional.of(mockSchedule));

        when(repository.findAllByCollaboratorIdAndDateTimeBetween(eq(collaboratorId), any(), any())).thenReturn(List.of(mockAppointment));

        AvailableTimesDto result = appointmentService.findAvailableTimes(collaboratorId, serviceId, date);

        assertNotNull(result);
        assertEquals(0, result.availableTimes().size());
    }
    @Test
    @DisplayName("Deve criar agendamento com sucesso.")
    void shouldCreateAppointment(){
        Long collaboratorId = 1L;
        Long serviceId = 1L;
        Long clientId = 1L;
        String clientPhone = "35911112222";
        String clientName = "Teste";
        Long appId = 1L;
        LocalDateTime appDateTime = LocalDateTime.of(2026,9,28,9,0,0);

        CreateAppointmentDTO dto = new CreateAppointmentDTO(collaboratorId, serviceId, appDateTime, clientName, clientPhone);


        Collaborator mockCollaborator = new Collaborator();
        mockCollaborator.setId(collaboratorId);

        Service mockService = new Service();
        mockService.setId(serviceId);

        Client mockClient = new Client();
        mockClient.setId(clientId);
        mockClient.setName(clientName);
        mockClient.setPhone(clientPhone);

        Appointment mockAppointment = new Appointment(mockClient, mockCollaborator, mockService, appDateTime, AppointmentStatus.SCHEDULED);
        mockAppointment.setId(1L);

        when(collaboratorRepository.findById(collaboratorId)).thenReturn(Optional.of(mockCollaborator));
        when(serviceRepository.findById(serviceId)).thenReturn(Optional.of(mockService));
        when(clientRepository.findByPhone(clientPhone)).thenReturn(Optional.empty());

        when(clientRepository.save(any())).thenReturn(mockClient);
        when(repository.save(any())).thenReturn(mockAppointment);

        AppointmentDetailsDTO result = appointmentService.createAppointment(dto);

        assertNotNull(result);
        assertEquals(1L, result.id());

        verify(repository, times(1)).save(any(Appointment.class));
        verify(clientRepository, times(1)).save(any(Client.class));
    }
}

