package com.example.cinematicketbookingapp.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record ScreeningListDto(Long screeningId,String movieTitle, LocalDateTime screeningStartDateTime) {
    @Builder(toBuilder = true)
    public ScreeningListDto {
    }
}
