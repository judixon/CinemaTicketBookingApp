package com.example.cinematicketbookingapp.config;

import java.math.BigDecimal;
import java.time.Period;

public class GlobalValues {
    public static final Period reservationExpirationTime = Period.ofDays(3);
    public static final BigDecimal adultTicketPrice = BigDecimal.valueOf(25);
    public static final BigDecimal studentTicketPrice = BigDecimal.valueOf(18);
    public static final BigDecimal childTicketPrice = BigDecimal.valueOf(12.50);
}
