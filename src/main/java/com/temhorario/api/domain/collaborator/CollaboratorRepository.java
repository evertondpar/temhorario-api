package com.temhorario.api.domain.collaborator;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CollaboratorRepository extends JpaRepository<Collaborator, Long> {
    //vai executar SELECT * FROM collaborators WHERE active = true
    List<Collaborator> findAllByActiveTrue();
}
