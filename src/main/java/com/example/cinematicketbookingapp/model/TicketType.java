package com.example.cinematicketbookingapp.model;

import com.example.cinematicketbookingapp.config.GlobalValues;

import java.math.BigDecimal;

public enum TicketType {
    ADULT(GlobalValues.adultTicketPrice),
    STUDENT(GlobalValues.studentTicketPrice),
    CHILD(GlobalValues.childTicketPrice);

    BigDecimal price;

    TicketType(BigDecimal price) {
        this.price = price;
    }
}
