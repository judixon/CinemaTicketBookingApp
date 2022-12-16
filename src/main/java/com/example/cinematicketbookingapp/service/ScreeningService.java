package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.dto.ScreeningDetailsDto;
import com.example.cinematicketbookingapp.dto.ScreeningListDto;
import com.example.cinematicketbookingapp.dto.SeatListDto;
import com.example.cinematicketbookingapp.exceptions.DefaultExceptionMessages;
import com.example.cinematicketbookingapp.exceptions.ResourceNotFoundException;
import com.example.cinematicketbookingapp.mapper.ScreeningDtoMapper;
import com.example.cinematicketbookingapp.mapper.SeatDtoMapper;
import com.example.cinematicketbookingapp.model.Seat;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final ScreeningDtoMapper screeningDtoMapper;
    private final SeatRepository seatRepository;
    private final SeatDtoMapper seatDtoMapper;

    public List<ScreeningListDto> getScreenings(String firstSortingParam, String secondSortingParam,
                                                Sort.Direction firstParamDirection, Sort.Direction secondParamDirection,
                                                LocalDateTime fromDateTime, LocalDateTime toDateTime) {

        return screeningRepository.findAllByStartDateTimeAfterAndStartDateTimeBefore(
                        Sort.by(firstParamDirection, firstSortingParam)
                                .and(Sort.by(secondParamDirection, secondSortingParam)),
                        fromDateTime, toDateTime)
                .stream()
                .map(screeningDtoMapper::mapToScreeningListDto)
                .toList();
    }

    public ScreeningDetailsDto getScreening(Long screeningId) {
        return screeningRepository.findById(screeningId)
                .map(screeningDtoMapper::mapToScreeningDetailsDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(DefaultExceptionMessages.SCREENING_NOT_FOUND_EXCEPTION_MESSAGE, screeningId)))
                .toBuilder()
                .seatsAvailabilitySchema(createSeatsAvailabilitySchema(screeningId))
                .availableSeats(getListOfFreeSeats(screeningId))
                .build();
    }

    private List<String> createSeatsAvailabilitySchema(Long screeningId) {
        List<Seat> seats = seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(screeningId);
        StringBuilder rowPattern = new StringBuilder();
        List<String> result = new ArrayList<>();
        int tempRowNumber = 1;
        for (Seat seat : seats) {
            if (tempRowNumber != seat.getRowNumber()) {
                result.add(rowPattern.toString());
                rowPattern = new StringBuilder();
                tempRowNumber = seat.getRowNumber();
            }
            rowPattern.append(getSeatSignature(seat, screeningId));
        }
        result.add(rowPattern.toString());
        return result;
    }

    private String getSeatSignature(Seat seat, Long screeningId) {
        return seat.getReservations().stream()
                .map(reservation -> reservation.getScreening().getId())
                .anyMatch(aLong -> aLong.equals(screeningId)) ?
                String.format("%-5s", "(X)") :
                String.format("%-5s", String.format("[%s]", seat.getSeatNumber()));
    }

    private List<SeatListDto> getListOfFreeSeats(Long screeningId) {
        return seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(screeningId)
                .stream()
                .filter(seat -> seat.getReservations().stream()
                        .noneMatch(reservation -> reservation.getScreening().getId().equals(screeningId)))
                .map(seatDtoMapper::mapToSeatListDto)
                .toList();
    }
}
