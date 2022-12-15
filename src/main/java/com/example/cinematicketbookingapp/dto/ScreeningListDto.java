package com.example.cinematicketbookingapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

public record ScreeningListDto(Long screeningId, String movieTitle,
                               @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime screeningStartDateTime) {
    @Builder(toBuilder = true)
    public ScreeningListDto {
    }
}
