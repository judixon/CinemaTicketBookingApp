package com.example.cinematicketbookingapp.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationSummaryDto(Long reservationId, BigDecimal totalPrice,
                                    LocalDateTime expirationTime) {
}
