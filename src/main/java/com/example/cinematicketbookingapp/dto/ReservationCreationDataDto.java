package com.example.cinematicketbookingapp.dto;

import com.example.cinematicketbookingapp.model.Seat;
import com.example.cinematicketbookingapp.model.TicketType;

import java.util.List;
import java.util.Set;

public record ReservationCreationDataDto(Long screeningId, Set<Seat> seats, List<TicketType> ticketTypes, String ownerName, String ownerSurname) {
}
