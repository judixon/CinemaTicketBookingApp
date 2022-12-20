package com.example.cinematicketbookingapp.model;

import com.example.cinematicketbookingapp.config.AppFunctionalValuesConstants;

import java.math.BigDecimal;

public enum TicketType {
    ADULT(AppFunctionalValuesConstants.ADULT_TICKET_PRICE),
    STUDENT(AppFunctionalValuesConstants.STUDENT_TICKET_PRICE),
    CHILD(AppFunctionalValuesConstants.CHILD_TICKET_PRICE);

    BigDecimal price;

    TicketType(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
