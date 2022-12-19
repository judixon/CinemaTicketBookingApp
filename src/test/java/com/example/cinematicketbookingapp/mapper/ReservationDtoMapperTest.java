package com.example.cinematicketbookingapp.mapper;

import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.exceptions.ResourceNotFoundException;
import com.example.cinematicketbookingapp.model.Screening;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationDtoMapperTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private SeatRepository seatRepository;

    @InjectMocks
    private ReservationDtoMapper reservationDtoMapper;

    @Test
    void mapToReservation_shouldThrowResourceNotFoundException_whenScreeningWithGivenIdDoesNotExistsInRepository() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .screeningId(1L)
                .seatIds(Set.of(1L, 2L, 3L))
                .build();

        //when
        when(screeningRepository.findById(1L)).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceNotFoundException.class, () -> reservationDtoMapper.mapToReservation(reservationCreationDataDto));
    }

    @Test
    void mapToReservation_shouldThrowResourceNotFoundException_whenSeatsWithGivenIdsDoNotExistsInRepository() {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .screeningId(1L)
                .seatIds(Set.of(1L, 2L, 3L))
                .build();

        //when
        when(screeningRepository.findById(1L)).thenReturn(Optional.of(Screening.builder().build()));
        when(seatRepository.findById(anyLong())).thenReturn(Optional.empty());

        //then
        assertThrows(ResourceNotFoundException.class, () -> reservationDtoMapper.mapToReservation(reservationCreationDataDto));
    }
}