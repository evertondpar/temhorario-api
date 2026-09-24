package com.temhorario.api.domain.client;

public record ClientDetailsDTO(Long id, String name, String phone) {
    public ClientDetailsDTO(Client client){
        this(client.getId(), client.getName(), client.getPhone());
    }
}
