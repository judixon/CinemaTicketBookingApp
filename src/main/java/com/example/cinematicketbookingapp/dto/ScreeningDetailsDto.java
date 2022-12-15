package com.example.cinematicketbookingapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;


public record ScreeningDetailsDto(Long screeningId, String movieTitle,
                                  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")LocalDateTime screeningStartDateTime,
                                  @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")LocalDateTime screeningEndDateTime,
                                  List<String> ticketTypes, SeatsAvailabilitySchemaDto seatsAvailabilitySchemaDto) {
    @Builder(toBuilder = true)
    public ScreeningDetailsDto {
    }
}
