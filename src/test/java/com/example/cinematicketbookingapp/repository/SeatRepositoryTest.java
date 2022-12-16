package com.example.cinematicketbookingapp.repository;

import com.example.cinematicketbookingapp.model.Seat;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SeatRepositoryTest {

    @Autowired
    private SeatRepository seatRepository;

    @Nested
    @DataJpaTest
    class findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending {

        @Test
        void shouldReturnListWithAllScreeningRoomSeatsForParticularScreening() {
            //given
            Long screeningId = 1L;

            //when
            List<Seat> result = seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(screeningId);

            //then
            assertThat(result).hasSize(100);
        }

        @Test
        void shouldReturnListWithAllScreeningRoomSeatsForParticularScreeningSortedByRowNumberAscending(){
            //given
            Long screeningId = 1L;

            //when
            List<Seat> result = seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(screeningId);

            //then
            assertThat(result).isNotEmpty();
            assertThat(result).isSortedAccordingTo(Comparator.comparing(Seat::getRowNumber));
        }

        @Test
        void shouldReturnListWithAllScreeningRoomSeatsForParticularScreeningSortedBySeatNumberAscending(){
            //given
            Long screeningId = 3L;

            //when
            List<Seat> result = seatRepository.findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(screeningId);

            //then
            assertThat(result).isNotEmpty();
            assertThat(result).isSortedAccordingTo(Comparator.comparing(Seat::getSeatNumber));
        }
    }
}