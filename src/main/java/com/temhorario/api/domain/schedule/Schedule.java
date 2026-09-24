package com.temhorario.api.domain.schedule;

import com.temhorario.api.domain.collaborator.Collaborator;
import jakarta.persistence.*;
import lombok.*;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Table(name = "schedules")
@Entity(name = "Schedule")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "collaborator_id")
    private Collaborator collaborator;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime startTime;

    private LocalTime endTime;

    private Boolean active;
}
