package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;
import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.dto.ReservationSummaryDto;
import com.example.cinematicketbookingapp.exceptions.ReservationSystemClosedException;
import com.example.cinematicketbookingapp.exceptions.ReservedSeatsNumberUnequalToTicketsNumberException;
import com.example.cinematicketbookingapp.exceptions.SeatAlreadyReservedException;
import com.example.cinematicketbookingapp.exceptions.SingleUnreservedSeatLeftException;
import com.example.cinematicketbookingapp.mapper.ReservationDtoMapper;
import com.example.cinematicketbookingapp.model.Reservation;
import com.example.cinematicketbookingapp.model.Screening;
import com.example.cinematicketbookingapp.model.Seat;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    private ReservationDtoMapper reservationDtoMapper;

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ReservationService reservationService;


    @Test
    void createReservation_shouldThrowReservationSystemClosedException_whenReservationTimeIsAfterBorderTime() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder().build();
        Screening screening = Screening.builder()
                .startDateTime(LocalDateTime.now().minusMinutes(5))
                .build();
        Reservation reservation = Reservation.builder()
                .screening(screening)
                .build();

        //when
        when(reservationDtoMapper.mapToReservation(any(ReservationCreationDataDto.class))).thenReturn(reservation);

        //then
        assertThrows(ReservationSystemClosedException.class, () -> reservationService.createReservation(reservationCreationDataDto));
    }

    @Test
    void createReservation_shouldThrowReservedSeatsNumberUnequalToTicketsNumberException_whenNumberOfTicketsInUnequalToNumberOfChoseSeats() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .numberOfAdultTickets(1)
                .numberOfChildTickets(1)
                .numberOfStudentTickets(1)
                .seatIds(Set.of(1L, 2L, 3L, 4L, 5L))
                .build();
        Screening screening = Screening.builder()
                .startDateTime(LocalDateTime.MAX)
                .build();
        Reservation reservation = Reservation.builder()
                .screening(screening)
                .build();

        //when
        when(reservationDtoMapper.mapToReservation(any(ReservationCreationDataDto.class))).thenReturn(reservation);

        //then
        assertThrows(ReservedSeatsNumberUnequalToTicketsNumberException.class, () -> reservationService.createReservation(reservationCreationDataDto));
    }

    @Test
    void createReservation_shouldThrowSeatAlreadyReservedException_whenAtLeastOneOfChosenSeatsIsAlreadyReserved() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .screeningId(1L)
                .seatIds(Set.of())
                .build();
        Screening screening = Screening.builder()
                .id(1L)
                .startDateTime(LocalDateTime.MAX)
                .build();
        Reservation existingReservationForTheSameScreening = Reservation.builder()
                .screening(screening)
                .build();
        Seat beingReservedSeat = Seat.builder()
                .reservations(Set.of(existingReservationForTheSameScreening))
                .build();
        Reservation beingCreatedReservation = Reservation.builder()
                .screening(screening)
                .seats(Set.of(beingReservedSeat))
                .build();

        //when
        when(reservationDtoMapper.mapToReservation(any(ReservationCreationDataDto.class))).thenReturn(beingCreatedReservation);

        //then
        assertThrows(SeatAlreadyReservedException.class, () -> reservationService.createReservation(reservationCreationDataDto));
    }

    @Test
    void createReservation_shouldThrowSingleUnreservedSeatLeftException_whenSingleUnreservedSeatIsLeftAtTheBeginningOfTheRow() {
       //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .screeningId(1L)
                .seatIds(Set.of())
                .build();
        Screening screening = Screening.builder()
                .id(1L)
                .startDateTime(LocalDateTime.MAX)
                .build();
        Reservation existingReservationForTheSameScreening = Reservation.builder()
                .screening(screening)
                .build();
        Seat seat1 = Seat.builder()
                .id(1L)
                .rowNumber(1)
                .seatNumber(1)
                .reservations(Set.of())
                .build();
        Seat seat2 = Seat.builder()
                .id(2L)
                .rowNumber(1)
                .seatNumber(2)
                .reservations(Set.of())
                .build();
        Seat seat3 = Seat.builder()
                .id(3L)
                .rowNumber(1)
                .seatNumber(3)
                .reservations(Set.of(existingReservationForTheSameScreening))
                .build();
        Reservation beingCreatedReservation = Reservation.builder()
                .screening(screening)
                .seats(Set.of(seat2))
                .build();

        //when
        when(reservationDtoMapper.mapToReservation(any(ReservationCreationDataDto.class))).thenReturn(beingCreatedReservation);
        when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                .thenReturn(List.of(seat1,seat2,seat3));

        //then
        assertThrows(SingleUnreservedSeatLeftException.class, ()->reservationService.createReservation(reservationCreationDataDto));
    }

    @Test
    void createReservation_shouldThrowSingleUnreservedSeatLeftException_whenSingleUnreservedSeatIsLeftAtTheEndOfTheRow() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .screeningId(1L)
                .seatIds(Set.of())
                .build();
        Screening screening = Screening.builder()
                .id(1L)
                .startDateTime(LocalDateTime.MAX)
                .build();
        Reservation existingReservationForTheSameScreening = Reservation.builder()
                .screening(screening)
                .build();
        Seat seat1 = Seat.builder()
                .id(1L)
                .rowNumber(1)
                .seatNumber(1)
                .reservations(Set.of(existingReservationForTheSameScreening))
                .build();
        Seat seat2 = Seat.builder()
                .id(2L)
                .rowNumber(1)
                .seatNumber(2)
                .reservations(Set.of())
                .build();
        Seat seat3 = Seat.builder()
                .id(3L)
                .rowNumber(1)
                .seatNumber(3)
                .reservations(Set.of())
                .build();
        Reservation beingCreatedReservation = Reservation.builder()
                .screening(screening)
                .seats(Set.of(seat2))
                .build();

        //when
        when(reservationDtoMapper.mapToReservation(any(ReservationCreationDataDto.class))).thenReturn(beingCreatedReservation);
        when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                .thenReturn(List.of(seat1,seat2,seat3));

        //then
        assertThrows(SingleUnreservedSeatLeftException.class, ()->reservationService.createReservation(reservationCreationDataDto));
    }

    @Test
    void createReservation_shouldThrowSingleUnreservedSeatLeftException_whenSingleUnreservedSeatIsLeftBetweenTwoPotentiallyReserved() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .screeningId(1L)
                .seatIds(Set.of())
                .build();
        Screening screening = Screening.builder()
                .id(1L)
                .startDateTime(LocalDateTime.MAX)
                .build();
        Reservation existingReservationForTheSameScreening = Reservation.builder()
                .screening(screening)
                .build();
        Seat seat1 = Seat.builder()
                .id(1L)
                .rowNumber(1)
                .seatNumber(1)
                .reservations(Set.of())
                .build();
        Seat seat2 = Seat.builder()
                .id(2L)
                .rowNumber(1)
                .seatNumber(2)
                .reservations(Set.of())
                .build();
        Seat seat3 = Seat.builder()
                .id(3L)
                .rowNumber(1)
                .seatNumber(3)
                .reservations(Set.of(existingReservationForTheSameScreening))
                .build();
        Reservation beingCreatedReservation = Reservation.builder()
                .screening(screening)
                .seats(Set.of(seat1))
                .build();

        //when
        when(reservationDtoMapper.mapToReservation(any(ReservationCreationDataDto.class))).thenReturn(beingCreatedReservation);
        when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                .thenReturn(List.of(seat1,seat2,seat3));

        //then
        assertThrows(SingleUnreservedSeatLeftException.class, ()->reservationService.createReservation(reservationCreationDataDto));
    }

    @Test

    void createReservation_shouldCorrectlyCountTotalPriceForTickets_whenChosenTicketsNumberIsEqualToNumberOfChosenSeats() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .screeningId(1L)
                .numberOfStudentTickets(1)
                .numberOfAdultTickets(1)
                .numberOfChildTickets(1)
                .seatIds(Set.of(1L,2L,3L))
                .build();
        Screening screening = Screening.builder()
                .id(1L)
                .startDateTime(LocalDateTime.MAX)
                .build();
        Seat seat1 = Seat.builder()
                .id(1L)
                .rowNumber(1)
                .seatNumber(1)
                .reservations(Set.of())
                .build();
        Seat seat2 = Seat.builder()
                .id(2L)
                .rowNumber(1)
                .seatNumber(2)
                .reservations(Set.of())
                .build();
        Seat seat3 = Seat.builder()
                .id(3L)
                .rowNumber(1)
                .seatNumber(3)
                .reservations(Set.of())
                .build();
        Reservation beingCreatedReservation = Reservation.builder()
                .screening(screening)
                .seats(Set.of(seat1,seat2,seat3))
                .build();
        BigDecimal expectedResult = BigDecimal.valueOf(55.5);

        //when
        when(reservationDtoMapper.mapToReservation(any())).thenReturn(beingCreatedReservation);
        when(reservationDtoMapper.mapToReservationSummaryDto(any())).thenReturn(ReservationSummaryDto.builder().build());
        when(reservationRepository.save(any(Reservation.class))).thenReturn(beingCreatedReservation);
        when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                .thenReturn(List.of(seat1,seat2,seat3));
        BigDecimal realResult = reservationService.createReservation(reservationCreationDataDto).totalPrice();

        //then
        verify(reservationRepository).save(any(Reservation.class));
        assertThat(realResult.toString()).isEqualTo(expectedResult.toString());
    }
}