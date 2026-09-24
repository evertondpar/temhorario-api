package com.temhorario.api.domain.collaborator;

import com.temhorario.api.domain.service.Service;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Table(name = "collaborators")
@Entity(name = "Collaborator")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Collaborator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;

    @ManyToMany
    @JoinTable(
            name = "collaborator_services",
            joinColumns = @JoinColumn(name = "collaborator_id"),
            inverseJoinColumns = @JoinColumn(name = "service_id")
    )
    private List<Service> services = new ArrayList<>();

    private Boolean active;

    public Collaborator (CreateCollaboratorDTO data, List<Service> services){
        this.name = data.name();
        this.phone = data.phone();
        this.services = services;
        this.active = true;
    }
}
