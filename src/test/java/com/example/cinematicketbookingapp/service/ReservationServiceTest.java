package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;
import com.example.cinematicketbookingapp.exceptions.ReservationSystemClosedException;
import com.example.cinematicketbookingapp.mapper.ReservationDtoMapper;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationDtoMapper reservationDtoMapper;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private AppFunctionalValues appFunctionalValues;

    @InjectMocks
    private ReservationService reservationService;


    @Test
    void createReservation_shouldThrowReservationSystemClosedException_whenReservationTimeIsAfterBorderTime(){
        //given

        //when


        //then
//        assertThrows(ReservationSystemClosedException.class,() -> )
    }

    @Test
    void createReservation_shouldThrowReservedSeatsNumberUnequalToTicketsNumberException_whenNumberOfTicketsInUnequalToNumberOfChoseSeats(){
        //given

        //when


        //then
//        assertThrows(ReservationSystemClosedException.class,() -> )
    }
    @Test
    void createReservation_shouldThrowSeatAlreadyReservedException_whenAtLeastOneOfChosenSeatsIsAlreadyReserved(){
        //given

        //when


        //then
//        assertThrows(ReservationSystemClosedException.class,() -> )
    }

    @Test
    void createReservation_shouldThrowSingleUnreservedSeatLeftException_whenSingleUnreservedSeatIsLeftAtTheBeginningOfTheRow(){

    }

    @Test
    void createReservation_shouldThrowSingleUnreservedSeatLeftException_whenSingleUnreservedSeatIsLeftAtTheEndOfTheRow(){

    }

    @Test
    void createReservation_shouldThrowSingleUnreservedSeatLeftException_whenSingleUnreservedSeatIsLeftBetweenTwoPotentiallyReserved(){

    }

    @Test
    void createReservation_shouldCorrectlyCountTotalPriceForTickets_whenChosenTicketsNumberIsEqualToNumberOfChosenSeats(){

    }
}