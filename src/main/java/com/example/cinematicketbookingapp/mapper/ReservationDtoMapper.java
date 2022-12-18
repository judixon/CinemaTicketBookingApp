package com.example.cinematicketbookingapp.mapper;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;
import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.dto.ReservationSummaryDto;
import com.example.cinematicketbookingapp.exceptions.ResourceNotFoundException;
import com.example.cinematicketbookingapp.model.Reservation;
import com.example.cinematicketbookingapp.model.Screening;
import com.example.cinematicketbookingapp.model.Seat;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationDtoMapper {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;

    public Reservation mapToReservation(ReservationCreationDataDto reservationCreationDataDto) {
        return Reservation.builder()
                .screening(getScreeningById(reservationCreationDataDto.screeningId()))
                .ownerName(reservationCreationDataDto.ownerName())
                .ownerSurname(reservationCreationDataDto.ownerSurname())
                .creationDateTime(LocalDateTime.now())
                .seats(getSeatsById(reservationCreationDataDto.seatIds()))
                .build();
    }

    public ReservationSummaryDto mapToReservationSummaryDto(Reservation reservation) {
        return ReservationSummaryDto.builder()
                .reservationId(reservation.getId())
                .expirationTime(reservation.getCreationDateTime().plus(AppFunctionalValues.RESERVATION_EXPIRATION_TIME))
                .build();
    }

    private Screening getScreeningById(Long id) {
        return screeningRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Screening", id));
    }

    private Set<Seat> getSeatsById(Set<Long> seatIds) {
        return seatIds.stream()
                .map(seatId -> seatRepository.findById(seatId)
                        .orElseThrow(() -> new ResourceNotFoundException("Seat", seatId)))
                .collect(Collectors.toSet());
    }
}
