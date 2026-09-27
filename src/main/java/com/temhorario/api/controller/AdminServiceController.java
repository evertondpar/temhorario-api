package com.temhorario.api.controller;

import com.temhorario.api.domain.service.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/admin/services")
public class AdminServiceController {
    private final ServiceRepository repository;

    public AdminServiceController(ServiceRepository repository){
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ServiceDetailsDTO> create(
            @RequestBody @Valid CreateServiceDTO data,
            UriComponentsBuilder uriBuilder
            ){
        Service service = new Service(data);
        repository.save(service);

        URI uri = uriBuilder.path("/services/{id}").buildAndExpand(service.getId()).toUri();
        return ResponseEntity.created(uri).body(new ServiceDetailsDTO(service));
    }

    @GetMapping
    public ResponseEntity<List<ServiceDetailsDTO>> listAll() {
        var list = repository.findAllByActiveTrue()
                .stream()
                .map(ServiceDetailsDTO::new)
                .toList();

        return ResponseEntity.ok(list);
    }

    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<ServiceDetailsDTO> update(@PathVariable Long id, @RequestBody @Valid UpdateServiceDTO data){
        var service = repository.getReferenceById(id);

        if(data.name() != null) service.setName(data.name());
        if(data.durationInMinutes() != null) service.setDurationInMinutes(data.durationInMinutes());
        if(data.price() != null) service.setPrice(data.price());

        return ResponseEntity.ok(new ServiceDetailsDTO(service));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> delete(@PathVariable Long id){
        var service = repository.getReferenceById(id);

        service.setActive(false);

        return ResponseEntity.noContent().build();
    }
}
