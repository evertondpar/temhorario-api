package com.temhorario.api.domain.collaborator;

import com.temhorario.api.domain.service.ServiceDetailsDTO;

import java.util.List;

public record CollaboratorDetailsDTO(Long id, String name, String phone, List<ServiceDetailsDTO> services) {
    public CollaboratorDetailsDTO(Collaborator collaborator){
        this(collaborator.getId(),collaborator.getName(), collaborator.getPhone(), collaborator.getServices().stream().map(ServiceDetailsDTO::new).toList());
    }
}
