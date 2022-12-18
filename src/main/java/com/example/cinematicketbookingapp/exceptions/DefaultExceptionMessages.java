package com.example.cinematicketbookingapp.exceptions;

public abstract class DefaultExceptionMessages {
    public static final String RESOURCE_NOT_FOUND_EXCEPTION_MESSAGE = "%s with ID: %s not found";

    public static final String RESERVATION_SYSTEM_CLOSED_EXCEPTION_MESSAGE = "Creating reservation impossible, reservation system" +
            "closes %s %s before screening";

    public static final String RESERVED_SEATS_AMOUNT_UNEQUAL_TO_TICKETS_AMOUNT_EXCEPTION_MESSAGE="Amount of reserved seats " +
            "(%s) is not equal amount of chosen tickets (%s).";

    public static final String SEAT_ALREADY_RESERVED_EXCEPTION = "Seat with number: %s (ID: %s) is already reserved";

    public static final String SINGLE_UNRESERVED_SEAT_LEFT_EXCEPTION_MESSAGE = "Single unreserved seat with number: %s " +
            "(ID: %s) left.";
}
