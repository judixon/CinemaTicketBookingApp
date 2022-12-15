package com.example.cinematicketbookingapp.dto;

import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


public record ScreeningDetailsDto(Long screeningId, String movieTitle, LocalDateTime screeningStartDateTime,
                                  LocalDateTime screeningEndDateTime, List<String> ticketTypes,
                                  SeatsAvailabilitySchemaDto seatsAvailabilitySchemaDto) {
    @Builder(toBuilder = true)
    public ScreeningDetailsDto {
    }
}
