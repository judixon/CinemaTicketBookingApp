package com.example.cinematicketbookingapp.exceptions.globalexceptionhandler;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

public record ApiExceptionData(String message,
                               HttpStatus responseHttpStatus, ZonedDateTime timestamp) {
    @Builder(toBuilder = true)
    public ApiExceptionData {
    }
}
