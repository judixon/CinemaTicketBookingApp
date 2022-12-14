package com.example.cinematicketbookingapp.dto;

import com.example.cinematicketbookingapp.model.Seat;

import java.util.List;

public record ReservationCreationDataDto(Long screeningId, List<Seat> seatList, String ownerName, String ownerSurname) {
}
