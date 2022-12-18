package com.example.cinematicketbookingapp.exceptions.globalexceptionhandler;

import com.example.cinematicketbookingapp.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler(value = {ResourceNotFoundException.class})
    public ResponseEntity<Object> handleElementNotFoundException(ResourceNotFoundException e) {
        e.printStackTrace();
        ApiExceptionData apiExceptionData = new ApiExceptionData(
                e.getMessage(),
                HttpStatus.NOT_FOUND,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionData, apiExceptionData.responseHttpStatus());
    }

    @ExceptionHandler(value = {ReservationSystemClosedException.class, ReservedSeatsNumberUnequalToTicketsNumberException.class,
            SeatAlreadyReservedException.class, SingleUnreservedSeatLeftException.class})
    public ResponseEntity<Object> handleExceptionConnectedWithCreatingReservationProcess(ResourceNotFoundException e) {
        e.printStackTrace();
        ApiExceptionData apiExceptionData = new ApiExceptionData(
                e.getMessage(),
                HttpStatus.BAD_REQUEST,
                ZonedDateTime.now()
        );
        return new ResponseEntity<>(apiExceptionData, apiExceptionData.responseHttpStatus());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return errors;
    }

}
