package com.example.cinematicketbookingapp.controller;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
class ControllerTestConstantValues {

    static String CORRECT_USER_NAME = "Name";
    static String CORRECT_USER_SURNAME = "Surname-Surname";

    static String SCREENING_CONTROLLER_BEAN_NAME = "screeningController";
    static String RESERVATION_CONTROLLER_BEAN_NAME = "reservationController";

    static String FROM_DATE_TIME_PARAM = "fromDateTime";
    static String TO_DATE_TIME_PARAM = "toDateTime";

    static String RESERVATIONS_PATH = "/reservations";
    static String SCREENINGS_PATH = "/screenings";

    static String SCREENING_NOT_FOUND_EXCEPTION_MESSAGE = "Screening with ID: 100 not found.";
    static String SEAT_NOT_FOUND_EXCEPTION_MESSAGE = "Seat with ID: 9999 not found.";
    static String SEAT_ALREADY_RESERVED_EXCEPTION_MESSAGE = "Seat with number: 1 (ID: 1) is already reserved.";
    static String RESERVED_SEAT_NUMBER_UNEQUAL_TO_TICKET_NUMBER_MESSAGE = "Number of reserved seats " +
            "(1) is not equal to number of chosen tickets (2).";
    static String SINGLE_UNRESERVED_SEAT_LEFT_EXCEPTION = "Single unreserved seat with number: 10 " +
            "(ID: 10) left.";
    static String RESERVATION_SYSTEM_CLOSED_EXCEPTION_MESSAGE = "Creating reservation impossible, reservation system " +
            "closes  15 minutes before screening.";
}
