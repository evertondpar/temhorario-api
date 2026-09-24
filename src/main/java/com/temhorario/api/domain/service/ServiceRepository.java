package com.temhorario.api.domain.service;

import com.temhorario.api.domain.collaborator.Collaborator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findAllByActiveTrue();
}
