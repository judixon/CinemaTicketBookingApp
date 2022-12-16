package com.example.cinematicketbookingapp.dto;

import lombok.Builder;

public record SeatListDto(Long seatId, int seatNumber, int rowNumber, Long screeningRoomId) {
    @Builder(toBuilder = true)
    public SeatListDto {
    }
}
