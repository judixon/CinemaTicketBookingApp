package com.example.cinematicketbookingapp.model;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;

import java.math.BigDecimal;

public enum TicketType {
    ADULT(AppFunctionalValues.adultTicketPrice),
    STUDENT(AppFunctionalValues.studentTicketPrice),
    CHILD(AppFunctionalValues.childTicketPrice);

    BigDecimal price;

    TicketType(BigDecimal price) {
        this.price = price;
    }
}
