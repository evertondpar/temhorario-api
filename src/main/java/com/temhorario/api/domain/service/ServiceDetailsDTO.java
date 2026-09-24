package com.temhorario.api.domain.service;

import java.math.BigDecimal;

public record ServiceDetailsDTO(
        Long id,
        String name,
        Integer durationInMinutes,
        BigDecimal price,
        Boolean active
) {
    public ServiceDetailsDTO(Service service){
        this(
                service.getId(),
                service.getName(),
                service.getDurationInMinutes(),
                service.getPrice(),
                service.getActive()
        );
    }
}
