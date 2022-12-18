package com.example.cinematicketbookingapp.exceptions;

public class ReservedSeatsAmountUnequalToTicketsAmountException extends RuntimeException {
    public ReservedSeatsAmountUnequalToTicketsAmountException(int reservedSeatsAmount, int ticketsAmount) {
        super(String.format(
                DefaultExceptionMessages.RESERVED_SEATS_AMOUNT_UNEQUAL_TO_TICKETS_AMOUNT_EXCEPTION_MESSAGE, reservedSeatsAmount,
                ticketsAmount));
    }
}
