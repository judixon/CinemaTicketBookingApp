package com.example.cinematicketbookingapp.exceptions;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;

public class ReservationSystemClosedException extends RuntimeException{

    public ReservationSystemClosedException() {
        super(String.format(
                DefaultExceptionMessages.RESERVATION_SYSTEM_CLOSED_EXCEPTION_MESSAGE,
                AppFunctionalValues.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getHour()!=0?
                        AppFunctionalValues.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getHour()+" hours":"",
                AppFunctionalValues.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getHour()+" minute"));
    }
}
