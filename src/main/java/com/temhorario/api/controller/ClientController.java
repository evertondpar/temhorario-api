package com.temhorario.api.controller;

import com.temhorario.api.domain.client.Client;
import com.temhorario.api.domain.client.ClientDetailsDTO;
import com.temhorario.api.domain.client.ClientRepository;
import com.temhorario.api.domain.client.CreateClientDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("api/clients")
public class ClientController {

    private final ClientRepository repository;

    public ClientController(ClientRepository repository){
        this.repository = repository;
    }

    @PostMapping
    @Transactional
    public ResponseEntity<ClientDetailsDTO> create(
            @RequestBody @Valid CreateClientDTO data,
            UriComponentsBuilder uriBuilder
            ){
        Client client = new Client();
        client.setName(data.name());
        client.setPhone(data.phone());

        repository.save(client);

        URI uri = uriBuilder.path("/clients/{id}").buildAndExpand(client.getId()).toUri();
        return ResponseEntity.created(uri).body(new ClientDetailsDTO(client));
    }

    @GetMapping
    public ResponseEntity<List<ClientDetailsDTO>> listAll(){
        List<ClientDetailsDTO> list = repository.findAll().stream().map(ClientDetailsDTO::new).toList();
        return ResponseEntity.ok(list);
    }
}
