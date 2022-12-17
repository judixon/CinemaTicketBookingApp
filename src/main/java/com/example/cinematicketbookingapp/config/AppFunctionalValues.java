package com.example.cinematicketbookingapp.config;

import java.math.BigDecimal;
import java.time.LocalTime;
import java.time.Period;

public abstract class AppFunctionalValues {
    public static final Period RESERVATION_EXPIRATION_TIME = Period.ofDays(3);
    public static final BigDecimal ADULT_TICKET_PRICE = BigDecimal.valueOf(25);
    public static final BigDecimal STUDENT_TICKET_PRICE = BigDecimal.valueOf(18);
    public static final BigDecimal CHILD_TICKET_PRICE = BigDecimal.valueOf(12.50);
    public static final LocalTime BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME = LocalTime.of(0, 15);


}
