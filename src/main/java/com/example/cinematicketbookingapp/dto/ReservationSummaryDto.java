package com.example.cinematicketbookingapp.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationSummaryDto(Long reservationId, BigDecimal totalPrice,
                                    LocalDateTime expirationTime) {
}
