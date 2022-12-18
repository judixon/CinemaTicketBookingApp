package com.example.cinematicketbookingapp.controller;

import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.dto.ReservationSummaryDto;
import com.example.cinematicketbookingapp.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping
    ResponseEntity<ReservationSummaryDto> createReservation(@RequestBody ReservationCreationDataDto reservationCreationDataDto) {
        ReservationSummaryDto reservationSummaryDto = reservationService.createReservation(reservationCreationDataDto);
        URI savedReservationUri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{reservation-id}")
                .buildAndExpand(reservationSummaryDto.reservationId())
                .toUri();
        return ResponseEntity.created(savedReservationUri).body(reservationSummaryDto);
    }
}
