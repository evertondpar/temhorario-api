package com.temhorario.api.domain.client;

import jakarta.persistence.*;
import lombok.*;

@Table(name = "clients")
@Entity(name = "Client")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String phone;
}
