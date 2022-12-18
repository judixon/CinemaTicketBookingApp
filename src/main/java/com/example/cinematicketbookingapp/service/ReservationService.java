package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;
import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.dto.ReservationSummaryDto;
import com.example.cinematicketbookingapp.exceptions.ReservationSystemClosedException;
import com.example.cinematicketbookingapp.exceptions.ReservedSeatsAmountUnequalToTicketsAmountException;
import com.example.cinematicketbookingapp.exceptions.SeatAlreadyReservedException;
import com.example.cinematicketbookingapp.mapper.ReservationDtoMapper;
import com.example.cinematicketbookingapp.model.Reservation;
import com.example.cinematicketbookingapp.model.Screening;
import com.example.cinematicketbookingapp.model.Seat;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationDtoMapper reservationDtoMapper;
    private final SeatRepository seatRepository;

    @Transactional
    public ReservationSummaryDto createReservation(ReservationCreationDataDto reservationCreationDataDto) {
        Reservation reservation = reservationDtoMapper.mapToReservation(reservationCreationDataDto);

        checkIfReservationDateTimeIsBeforeScreeningReservationSystemClosureDateTime(reservation.getScreening());
        checkIfReservedSeatsAmountIsEqualToTicketsAmount(reservationCreationDataDto);
        checkIfChosenSeatsAreNotAlreadyReserved(reservation.getSeats(), reservationCreationDataDto.screeningId());
        checkIfUnreservedSingleSeatIsLeft(reservation.getSeats(),reservationCreationDataDto.screeningId());

        //jedna transakcja na raz może działać żeby nie można było zrobić rezerwacji na to samo miejsce w tym samym czasie dwukrotnie

        return reservationDtoMapper.mapToReservationSummaryDto(reservationRepository.save(reservation)).toBuilder()
                .totalPrice(countTotalPrize(reservationCreationDataDto))
                .build();
    }

    private BigDecimal countTotalPrize(ReservationCreationDataDto dto) {
        return AppFunctionalValues.ADULT_TICKET_PRICE.multiply(BigDecimal.valueOf(dto.amountOfAdultTickets()))
                .add(AppFunctionalValues.STUDENT_TICKET_PRICE.multiply(BigDecimal.valueOf(dto.amountOfStudentTickets())))
                .add(AppFunctionalValues.CHILD_TICKET_PRICE.multiply(BigDecimal.valueOf(dto.amountOfChildTickets())));
    }

    private void checkIfReservationDateTimeIsBeforeScreeningReservationSystemClosureDateTime(Screening screening) {
        if (LocalDateTime.now().isBefore(screening.getStartDateTime()
                .minusHours(AppFunctionalValues.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getHour())
                .minusMinutes(AppFunctionalValues.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getMinute()))) {
            throw new ReservationSystemClosedException();
        }
    }

    private void checkIfReservedSeatsAmountIsEqualToTicketsAmount(ReservationCreationDataDto reservationCreationDataDto) {
        int reservedSeatsAmount = reservationCreationDataDto.seatIds().size();
        int ticketsAmount = reservationCreationDataDto.amountOfAdultTickets()
                + reservationCreationDataDto.amountOfStudentTickets()
                + reservationCreationDataDto.amountOfChildTickets();
        if (reservedSeatsAmount != ticketsAmount) {
            throw new ReservedSeatsAmountUnequalToTicketsAmountException(reservedSeatsAmount, ticketsAmount);
        }
    }

    private void checkIfChosenSeatsAreNotAlreadyReserved(Set<Seat> seats, Long screeningId) {
        for (Seat seat : seats) {
            if(checkIfSeatIsReservedForParticularScreening(screeningId, seat)){
                throw new SeatAlreadyReservedException(seat.getSeatNumber(), seat.getId());
            }
        }
    }

    private void checkIfUnreservedSingleSeatIsLeft(Set<Seat> seats, Long screeningId) {
        List<Seat> screeningRoomSeatList = seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(screeningId);
        checkIfSingleUnreservedSeatIsLeftBetweenChosenSeats(seats,screeningRoomSeatList);
        checkIfSingleUnreservedSeatIsLeftBetweenChosenAndAlreadyReservedSeat(seats,screeningRoomSeatList);
        checkIfSingleUnreservedSeatIsLeftInTheEndOfTheRow(seats,screeningRoomSeatList);

    }

    private void checkIfSingleUnreservedSeatIsLeftInTheEndOfTheRow(Set<Seat> chosenSeats, List<Seat> screeningRoomSeats) {

    }

    private void checkIfSingleUnreservedSeatIsLeftBetweenChosenSeats(Set<Seat> chosenSeats, List<Seat> screeningRoomSeats){

    }

    private void checkIfSingleUnreservedSeatIsLeftBetweenChosenAndAlreadyReservedSeat(Set<Seat> chosenSeats, List<Seat> screeningRoomSeats){

    }

    private boolean checkIfSeatIsReservedForParticularScreening(Long screeningId, Seat seat) {
        return seat.getReservations().stream()
                .map(reservation -> reservation.getScreening().getId())
                .anyMatch(id -> id.equals(screeningId));
    }
}
