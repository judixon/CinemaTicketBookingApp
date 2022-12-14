package com.example.cinematicketbookingapp.exceptions;

public class SeatAlreadyReservedException extends RuntimeException {
    public SeatAlreadyReservedException(int seatNumber, Long seatId) {
        super(String.format(DefaultExceptionMessagesConstants.SEAT_ALREADY_RESERVED_EXCEPTION, seatNumber, seatId));
    }
}
