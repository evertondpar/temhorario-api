package com.temhorario.api.controller;

import com.temhorario.api.domain.appointment.*;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {

    private AppointmentService appointmentService;

    public AppointmentController(AppointmentService appointmentService){
        this.appointmentService = appointmentService;
    }

    @GetMapping("/available-times")
    public ResponseEntity<AvailableTimesDto> getAvailableTimes(
            @RequestParam Long collaboratorId,
            @RequestParam Long serviceId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
            ){
        AvailableTimesDto availableTimes = appointmentService.findAvailableTimes(collaboratorId, serviceId, date);
        return ResponseEntity.ok(availableTimes);
    }

    @PostMapping
    public ResponseEntity<AppointmentDetailsDTO> create(@RequestBody @Valid CreateAppointmentDTO data, UriComponentsBuilder uriBuilder){

        AppointmentDetailsDTO newAppointment = appointmentService.createAppointment(data);

        URI uri = uriBuilder.path("/api/appointments/{id}")
                .buildAndExpand(newAppointment.id())
                .toUri();
        return ResponseEntity.created(uri).body(newAppointment);
    }
}
