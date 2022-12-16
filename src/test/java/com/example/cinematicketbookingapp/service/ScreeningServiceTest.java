package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.dto.ScreeningDetailsDto;
import com.example.cinematicketbookingapp.dto.ScreeningListDto;
import com.example.cinematicketbookingapp.dto.SeatListDto;
import com.example.cinematicketbookingapp.exceptions.ResourceNotFoundException;
import com.example.cinematicketbookingapp.mapper.ScreeningDtoMapper;
import com.example.cinematicketbookingapp.mapper.SeatDtoMapper;
import com.example.cinematicketbookingapp.model.*;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.*;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ScreeningServiceTest {

    @Mock
    private ScreeningRepository screeningRepository;

    @Mock
    private SeatRepository seatRepository;

    @Mock
    private ScreeningDtoMapper screeningDtoMapper;

    @Mock
    private SeatDtoMapper seatDtoMapper;

    @InjectMocks
    private ScreeningService screeningService;

    @Test
    void getScreenings_returnsListOfScreeningsListDtoObjects() {
        //given

        //when
        when(screeningRepository.findAllByStartDateTimeAfterAndStartDateTimeBefore(any(), any(), any()))
                .thenReturn(List.of(new Screening(),new Screening(), new Screening()));
        when(screeningDtoMapper.mapToScreeningListDto(any(Screening.class))).thenReturn(ScreeningListDto.builder().build());
        List<ScreeningListDto> result = screeningService.getScreenings("a","b", Sort.Direction.ASC, Sort.Direction.ASC,
                LocalDateTime.now(),LocalDateTime.now());

        //then
        assertThat(result).hasSize(3);
    }

    @Nested
    class getScreening{

        @Test
        void returnsScreeningDetailsDtoWithEmptySeatList_whenAllSeatsAreReserved(){
            //given
            Screening screening = Screening.builder()
                    .id(1L)
                    .build();
            Reservation reservation = Reservation.builder()
                    .screening(screening)
                    .build();
            Seat seat1 = Seat.builder()
                    .reservations(Set.of(reservation))
                    .build();
            Seat seat2 = Seat.builder()
                    .reservations(Set.of(reservation))
                    .build();
            List<Seat> seatRepositoryMethodResult =  List.of(seat1,seat2);

            //when
            when(screeningRepository.findById(anyLong())).thenReturn(Optional.of(new Screening()));
            when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                    .thenReturn(seatRepositoryMethodResult);
            when(screeningDtoMapper.mapToScreeningDetailsDto(any(Screening.class)))
                    .thenReturn(ScreeningDetailsDto.builder().build());
             ScreeningDetailsDto result = screeningService.getScreening(1L);

            //then
            assertThat(result.availableSeats()).isEmpty();
        }

        @Test
        void throwsResourceNotFoundException_whenScreeningIsNotFound(){
            //given

            //when
            when(screeningRepository.findById(anyLong())).thenReturn(Optional.empty());

            //then
            assertThrows(ResourceNotFoundException.class,() -> screeningService.getScreening(1L));
        }

        @Test
        void returnsScreeningDetailsDtoWithFilledUpFreeSeatsList_whenSomeSeatsAreFree(){
            //given
            Screening screening1 = Screening.builder()
                    .id(1L)
                    .build();
            Screening screening2 = Screening.builder()
                    .id(2L)
                    .build();
            Reservation reservation1 = Reservation.builder()
                    .screening(screening1)
                    .build();
            Reservation reservation2 = Reservation.builder()
                    .screening(screening2)
                    .build();
            Seat seat1 = Seat.builder()
                    .reservations(Set.of(reservation1))
                    .build();
            Seat seat2 = Seat.builder()
                    .reservations(Set.of(reservation2))
                    .build();
            Seat seat3 = Seat.builder()
                    .reservations(Set.of())
                    .build();
            List<Seat> seatRepositoryMethodResult =  List.of(seat1,seat2,seat3);

            //when
            when(screeningRepository.findById(anyLong())).thenReturn(Optional.of(new Screening()));
            when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                    .thenReturn(seatRepositoryMethodResult);
            when(screeningDtoMapper.mapToScreeningDetailsDto(any(Screening.class)))
                    .thenReturn(ScreeningDetailsDto.builder().build());
            when(seatDtoMapper.mapToSeatListDto(any(Seat.class))).thenReturn(SeatListDto.builder().build());
            ScreeningDetailsDto result = screeningService.getScreening(1L);

            //then
            assertThat(result.availableSeats()).hasSize(2);
        }

        @Test
        void returnsEmptyStringListRepresentingScreeningRoomSeatSchema_whenScreeningRoomDoesNotHaveAnySeats(){
            //given

            //when
            when(screeningRepository.findById(anyLong())).thenReturn(Optional.of(new Screening()));
            when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                    .thenReturn(List.of());
            when(screeningDtoMapper.mapToScreeningDetailsDto(any(Screening.class)))
                    .thenReturn(ScreeningDetailsDto.builder().build());
            ScreeningDetailsDto result = screeningService.getScreening(1L);

            //then
            assertThat(result.seatsAvailabilitySchema()).isEmpty();
        }

        @Test
        void returnsCorrectStringListRepresentingScreeningRoomSeatSchema_whenScreeningRoomHasAnySeats(){
            //given
            Screening screening1 = Screening.builder()
                    .id(1L)
                    .build();
            Reservation reservation1 = Reservation.builder()
                    .screening(screening1)
                    .build();
            Seat seat1 = Seat.builder()
                    .rowNumber(1)
                    .seatNumber(1)
                    .reservations(Set.of())
                    .build();
            Seat seat2 = Seat.builder()
                    .rowNumber(1)
                    .seatNumber(12)
                    .reservations(Set.of())
                    .build();
            Seat seat3 = Seat.builder()
                    .rowNumber(2)
                    .seatNumber(7)
                    .reservations(Set.of())
                    .build();
            Seat seat4 = Seat.builder()
                    .rowNumber(5)
                    .seatNumber(999)
                    .reservations(Set.of(reservation1))
                    .build();
            List<Seat> seatRepositoryMethodResult =  List.of(seat1,seat2,seat3,seat4);
            String firstRowExpectation = "ROW(1):  [1]  [12] ";
            String secondRowExpectation = "ROW(2):  [7]  ";
            String thirdRowExpectation = "ROW(5):  (X)  ";

            //when
            when(screeningRepository.findById(anyLong())).thenReturn(Optional.of(new Screening()));
            when(seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(anyLong()))
                    .thenReturn(seatRepositoryMethodResult);
            when(screeningDtoMapper.mapToScreeningDetailsDto(any(Screening.class)))
                    .thenReturn(ScreeningDetailsDto.builder().build());
            List<String> result = screeningService.getScreening(1L).seatsAvailabilitySchema();

            //then
            assertThat(result.get(0)).isEqualTo(firstRowExpectation);
            assertThat(result.get(1)).isEqualTo(secondRowExpectation);
            assertThat(result.get(2)).isEqualTo(thirdRowExpectation);
        }
    }
}