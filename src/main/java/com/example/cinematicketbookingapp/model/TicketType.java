package com.example.cinematicketbookingapp.model;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;

import java.math.BigDecimal;

public enum TicketType {
    ADULT(AppFunctionalValues.ADULT_TICKET_PRICE),
    STUDENT(AppFunctionalValues.STUDENT_TICKET_PRICE),
    CHILD(AppFunctionalValues.CHILD_TICKET_PRICE);

    BigDecimal price;

    TicketType(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
