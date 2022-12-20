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
import com.example.cinematicketbookingapp.model.TicketType;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.LockModeType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;
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
        checkIfUnreservedSingleSeatIsLeft(reservation.getSeats(), reservationCreationDataDto.screeningId());

        return reservationDtoMapper.mapToReservationSummaryDto(reservationRepository.save(reservation)).toBuilder()
                .totalPrice(countTotalPrize(reservationCreationDataDto))
                .build();
    }

    private BigDecimal countTotalPrize(ReservationCreationDataDto dto) {
        return TicketType.ADULT.getPrice().multiply(BigDecimal.valueOf(dto.numberOfAdultTickets()))
                .add(TicketType.STUDENT.getPrice().multiply(BigDecimal.valueOf(dto.numberOfStudentTickets())))
                .add(TicketType.CHILD.getPrice().multiply(BigDecimal.valueOf(dto.numberOfChildTickets())));
    }

    private void checkIfReservationDateTimeIsBeforeScreeningReservationSystemClosureDateTime(Screening screening) {
        if (LocalDateTime.now().isAfter(screening.getStartDateTime()
                .minusHours(AppFunctionalValues.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getHour())
                .minusMinutes(AppFunctionalValues.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getMinute()))) {
            throw new ReservationSystemClosedException();
        }
    }

    private void checkIfReservedSeatsAmountIsEqualToTicketsAmount(ReservationCreationDataDto reservationCreationDataDto) {
        int reservedSeatsAmount = reservationCreationDataDto.seatIds().size();
        int ticketsAmount = reservationCreationDataDto.numberOfAdultTickets()
                + reservationCreationDataDto.numberOfStudentTickets()
                + reservationCreationDataDto.numberOfChildTickets();
        if (reservedSeatsAmount != ticketsAmount) {
            throw new ReservedSeatsNumberUnequalToTicketsNumberException(reservedSeatsAmount, ticketsAmount);
        }
    }

    private void checkIfChosenSeatsAreNotAlreadyReserved(Set<Seat> seats, Long screeningId) {
        for (Seat seat : seats) {
            if (checkIfSeatIsReservedForParticularScreening(screeningId, seat)) {
                throw new SeatAlreadyReservedException(seat.getSeatNumber(), seat.getId());
            }
        }
    }

    private void checkIfUnreservedSingleSeatIsLeft(Set<Seat> chosenSeats, Long screeningId) {
        List<Seat> screeningRoomSeatList = seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(screeningId);
        List<Integer> distinctRowNumbersFromChosenSeats = getDistinctRowNumbersFromChosenSeats(chosenSeats);

        for (Integer distinctRowNumber : distinctRowNumbersFromChosenSeats) {
            List<Seat> rowOfSeatsSortedBySeatNumber = getRowOfSeatsSortedBySeatNumber(distinctRowNumber, screeningRoomSeatList);
            if (rowOfSeatsSortedBySeatNumber.size() > 1) {
                for (int i = 0; i < rowOfSeatsSortedBySeatNumber.size(); i++) {
                    if (i == 0) {
                        checkIfTheSeatAtTheBeginningOfTheRowIsLeftFreeAsSingle(chosenSeats,
                                screeningId, rowOfSeatsSortedBySeatNumber, i);
                    } else if (i == rowOfSeatsSortedBySeatNumber.size() - 1) {
                        checkIfTheSeatAtTheEndOfTheRowIsLeftFreeAsSingle(chosenSeats, screeningId,
                                rowOfSeatsSortedBySeatNumber, i, i - 1);
                    } else {
                        checkIfTheSeatBetweenTwoOthersIsLeftFreeAsSingle(chosenSeats, screeningId, rowOfSeatsSortedBySeatNumber, i);
                    }
                }
            }
        }
    }

    private void checkIfTheSeatBetweenTwoOthersIsLeftFreeAsSingle(Set<Seat> chosenSeats, Long screeningId,
                                                                  List<Seat> rowOfSeatsSortedBySeatNumber, int i) {
        if (!checkIfSeatIsPotentiallyReserved(screeningId, rowOfSeatsSortedBySeatNumber.get(i), chosenSeats)) {
            if (checkIfSeatIsPotentiallyReserved(screeningId, rowOfSeatsSortedBySeatNumber.get(i - 1), chosenSeats) &&
                    checkIfSeatIsPotentiallyReserved(screeningId, rowOfSeatsSortedBySeatNumber.get(i + 1), chosenSeats)) {
                throw new SingleUnreservedSeatLeftException(rowOfSeatsSortedBySeatNumber.get(i).getId(),
                        rowOfSeatsSortedBySeatNumber.get(i).getSeatNumber());
            }
        }

    }

    private void checkIfTheSeatAtTheEndOfTheRowIsLeftFreeAsSingle(Set<Seat> chosenSeats, Long screeningId,
                                                                  List<Seat> rowOfSeatsSortedBySeatNumber,
                                                                  int currentSeatIndex, int previousSeatIndex) {
        if (!checkIfSeatIsPotentiallyReserved(screeningId, rowOfSeatsSortedBySeatNumber.get(currentSeatIndex), chosenSeats)) {
            if (checkIfSeatIsPotentiallyReserved(screeningId, rowOfSeatsSortedBySeatNumber.get(previousSeatIndex), chosenSeats)) {
                throw new SingleUnreservedSeatLeftException(rowOfSeatsSortedBySeatNumber.get(currentSeatIndex).getId(),
                        rowOfSeatsSortedBySeatNumber.get(currentSeatIndex).getSeatNumber());
            }
        }
    }

    private void checkIfTheSeatAtTheBeginningOfTheRowIsLeftFreeAsSingle(Set<Seat> chosenSeats, Long screeningId,
                                                                        List<Seat> rowOfSeatsSortedBySeatNumber,
                                                                        int currentSeatIndex) {
        checkIfTheSeatAtTheEndOfTheRowIsLeftFreeAsSingle(chosenSeats, screeningId, rowOfSeatsSortedBySeatNumber,
                currentSeatIndex, currentSeatIndex + 1);
    }

    private boolean checkIfSeatIsPotentiallyReserved(Long screeningId, Seat checkedSeat, Set<Seat> chosenToReserveSeats) {
        return checkIfSeatIsReservedForParticularScreening(screeningId, checkedSeat) ||
                checkIfBeingCheckedSeatIsOneOfChosenToReserve(checkedSeat, chosenToReserveSeats);
    }

    private boolean checkIfSeatIsReservedForParticularScreening(Long screeningId, Seat seat) {
        return seat.getReservations().stream()
                .map(reservation -> reservation.getScreening().getId())
                .anyMatch(id -> id.equals(screeningId));
    }

    private boolean checkIfBeingCheckedSeatIsOneOfChosenToReserve(Seat beingCheckedSeat, Set<Seat> chosenToReserveSeats) {
        return chosenToReserveSeats.contains(beingCheckedSeat);
    }

    private List<Integer> getDistinctRowNumbersFromChosenSeats(Set<Seat> chosenSeats) {
        return chosenSeats.stream()
                .map(Seat::getRowNumber)
                .distinct()
                .toList();
    }

    private List<Seat> getRowOfSeatsSortedBySeatNumber(int rowNumber, List<Seat> allSeats) {
        return allSeats.stream()
                .filter(seat -> seat.getRowNumber() == rowNumber)
                .sorted(Comparator.comparing(Seat::getSeatNumber))
                .toList();
    }
}
