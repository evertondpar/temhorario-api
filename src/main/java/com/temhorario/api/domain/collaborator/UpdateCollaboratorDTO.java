package com.temhorario.api.domain.collaborator;

import java.util.List;

public record UpdateCollaboratorDTO(String name, String phone, List<Long> serviceIds) {
}
