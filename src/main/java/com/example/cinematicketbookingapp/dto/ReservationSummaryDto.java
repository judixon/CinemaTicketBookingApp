package com.example.cinematicketbookingapp.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationSummaryDto(Long reservationId, BigDecimal totalPrice,
                                    LocalDateTime expirationTime) {
    @Builder(toBuilder = true)
    public ReservationSummaryDto {
    }
}
