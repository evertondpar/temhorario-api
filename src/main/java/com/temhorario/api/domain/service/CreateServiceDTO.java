package com.temhorario.api.domain.service;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record CreateServiceDTO(
        @NotBlank(message = "O nome é obrigatório")
        String name,

        @NotNull(message = "A duração é obrigatória")
        @Positive(message = "A duração deve ser maior que zero")
        Integer durationInMinutes,

        @NotNull(message = "O preço é obrigatório")
        @Positive(message = "O preço deve ser positivo")
        BigDecimal price
) {
}
