package com.temhorario.api.domain.service;

import java.math.BigDecimal;

public record UpdateServiceDTO(String name, Integer durationInMinutes, BigDecimal price) {
}
