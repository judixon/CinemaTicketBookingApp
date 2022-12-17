package com.example.cinematicketbookingapp.dto;

import java.util.Set;

public record ReservationCreationDataDto(
        Long screeningId, Set<Long> seatIds, String ownerName, String ownerSurname, int amountOfChildTickets,
        int amountOfStudentTickets, int amountOfAdultTickets) {
}
