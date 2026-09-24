package com.temhorario.api.domain.collaborator;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;

import java.util.List;

public record CreateCollaboratorDTO(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotBlank(message = "O telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve conter de 10 a 11 dígitos")
        String phone,

        @NotEmpty(message = "Informe ao menos um serviço para o colaborador")
        List<Long> serviceIds
) {
}
