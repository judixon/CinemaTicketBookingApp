package com.example.cinematicketbookingapp.exceptions;

public class ReservedSeatsNumberUnequalToTicketsNumberException extends RuntimeException {
    public ReservedSeatsNumberUnequalToTicketsNumberException(int reservedSeatsAmount, int ticketsAmount) {
        super(String.format(
                DefaultExceptionMessagesConstants.RESERVED_SEATS_NUMBER_UNEQUAL_TO_TICKETS_NUMBER_EXCEPTION_MESSAGE, reservedSeatsAmount,
                ticketsAmount));
    }
}
