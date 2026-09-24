package com.temhorario.api.domain.service;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Table(name = "services")
@Entity(name = "Service")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private Integer durationInMinutes;

    private BigDecimal price;

    private Boolean active;

    public Service(CreateServiceDTO data) {
        this.name = data.name();
        this.durationInMinutes = data.durationInMinutes();
        this.price = data.price();
        this.active = true;
    }
}
