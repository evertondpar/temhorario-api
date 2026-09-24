package com.temhorario.api.domain.appointment;

import com.temhorario.api.domain.client.Client;
import com.temhorario.api.domain.collaborator.Collaborator;
import com.temhorario.api.domain.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "appointments")
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
}
