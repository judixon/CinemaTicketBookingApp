package com.example.cinematicketbookingapp.dto;

import lombok.Builder;

import java.util.Set;

public record ReservationCreationDataDto(
        Long screeningId, Set<Long> seatIds, String ownerName, String ownerSurname, int numberOfChildTickets,
        int numberOfStudentTickets, int numberOfAdultTickets) {
    @Builder(toBuilder = true)
    public ReservationCreationDataDto {
    }
}
