package com.temhorario.api.controller;

import com.temhorario.api.domain.collaborator.*;
import com.temhorario.api.domain.schedule.CreateScheduleDTO;
import com.temhorario.api.domain.schedule.Schedule;
import com.temhorario.api.domain.schedule.ScheduleDetailsDTO;
import com.temhorario.api.domain.schedule.ScheduleRepository;
import com.temhorario.api.domain.service.Service;
import com.temhorario.api.domain.service.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/collaborators")
public class AdminCollaboratorController {
    private final CollaboratorRepository repository;
    private final ServiceRepository serviceRepository;
    private final ScheduleRepository scheduleRepository;
    public AdminCollaboratorController(CollaboratorRepository repository, ServiceRepository serviceRepository, ScheduleRepository scheduleRepository){
        this.repository = repository;
        this.serviceRepository = serviceRepository;
        this.scheduleRepository = scheduleRepository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<CollaboratorDetailsDTO> create(
            @RequestBody @Valid CreateCollaboratorDTO data,
            UriComponentsBuilder uriBuilder
            ) {
        List<Service> services = serviceRepository.findAllById(data.serviceIds());

        Collaborator collaborator = new Collaborator(data, services);
        repository.save(collaborator);

        URI uri = uriBuilder.path("/api/admin/collaborators/{id}").buildAndExpand(collaborator.getId()).toUri();
        return ResponseEntity.created(uri).body(new CollaboratorDetailsDTO(collaborator));
    }

    @PostMapping("/{id}/schedules")
    @Transactional
    public ResponseEntity<ScheduleDetailsDTO> createSchedule(
            @PathVariable Long id,
            @RequestBody @Valid CreateScheduleDTO data,
            UriComponentsBuilder uriBuilder
            ){
        var collaborator = repository.findById(id).orElseThrow(() -> new RuntimeException("Colaborador não encontrado"));

        Schedule schedule = new Schedule(data, collaborator);
        scheduleRepository.save(schedule);

        URI uri = uriBuilder.path("/api/admin/collaborators/{id}/schedules/{scheduleId}")
                .buildAndExpand(collaborator.getId(), schedule.getId())
                .toUri();

        return ResponseEntity.created(uri).body(new ScheduleDetailsDTO(schedule));
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<CollaboratorDetailsDTO> updateCollaborator(
            @PathVariable Long id, @RequestBody @Valid UpdateCollaboratorDTO data
    ){
        var collaborator = repository.getReferenceById(id);

        if(data.name() != null) collaborator.setName(data.name());
        if(data.phone() != null) collaborator.setPhone(data.phone());
        if(data.serviceIds() != null && !data.serviceIds().isEmpty()){
            List<Service> newServices = serviceRepository.findAllById(data.serviceIds());
            collaborator.setServices(newServices);
        }

        return ResponseEntity.ok(new CollaboratorDetailsDTO(collaborator));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id){
        var service = repository.getReferenceById(id);

        service.setActive(false);

        return ResponseEntity.noContent().build();
    }
}
