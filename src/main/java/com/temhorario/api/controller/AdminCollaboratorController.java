package com.temhorario.api.controller;

import com.temhorario.api.domain.collaborator.Collaborator;
import com.temhorario.api.domain.collaborator.CollaboratorDetailsDTO;
import com.temhorario.api.domain.collaborator.CollaboratorRepository;
import com.temhorario.api.domain.collaborator.CreateCollaboratorDTO;
import com.temhorario.api.domain.service.Service;
import com.temhorario.api.domain.service.ServiceRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/admin/collaborators")
public class AdminCollaboratorController {
    private final CollaboratorRepository repository;
    private final ServiceRepository serviceRepository;

    public AdminCollaboratorController(CollaboratorRepository repository, ServiceRepository serviceRepository){
        this.repository = repository;
        this.serviceRepository = serviceRepository;
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
}
