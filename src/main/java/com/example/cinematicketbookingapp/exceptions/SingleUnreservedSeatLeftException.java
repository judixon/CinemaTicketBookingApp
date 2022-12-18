package com.example.cinematicketbookingapp.exceptions;

public class SingleUnreservedSeatLeftException extends RuntimeException {
    public SingleUnreservedSeatLeftException(Long seatId, int seatNumber) {
        super(String.format(DefaultExceptionMessages.SINGLE_UNRESERVED_SEAT_LEFT_EXCEPTION_MESSAGE, seatNumber, seatId));
    }
}
