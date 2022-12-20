package com.example.cinematicketbookingapp.exceptions;

import com.example.cinematicketbookingapp.config.AppFunctionalValuesConstants;

public class ReservationSystemClosedException extends RuntimeException {

    public ReservationSystemClosedException() {
        super(String.format(
                DefaultExceptionMessagesConstants.RESERVATION_SYSTEM_CLOSED_EXCEPTION_MESSAGE,
                AppFunctionalValuesConstants.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getHour() != 0 ?
                        AppFunctionalValuesConstants.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getHour() + " hours" : "",
                AppFunctionalValuesConstants.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getMinute() + " minutes"));
    }
}
