package com.temhorario.api.domain.appointment;

import com.temhorario.api.domain.client.Client;
import com.temhorario.api.domain.collaborator.Collaborator;
import com.temhorario.api.domain.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Table(name = "appointments",
uniqueConstraints = {
        @UniqueConstraint(
                name = "uk_collaborator_time",
                columnNames = {"collaborator_id", "date_time"}
        )
})
@Entity(name = "Appointment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Appointment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    private LocalDateTime dateTime;

    @Enumerated(EnumType.STRING)
    private AppointmentStatus appointmentStatus;

    public Appointment(Client client, Collaborator collaborator, Service service, LocalDateTime dateTime, AppointmentStatus appointmentStatus){
        this.client = client;
        this.collaborator = collaborator;
        this.service = service;
        this.dateTime = dateTime;
        this.appointmentStatus = appointmentStatus;
    }

    public LocalTime getStartTime() {
        return this.dateTime.toLocalTime();
    }

    public LocalTime getEndTime() {
        return this.dateTime.toLocalTime().plusMinutes(this.service.getDurationInMinutes());
    }
}
