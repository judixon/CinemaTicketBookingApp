package com.example.cinematicketbookingapp.repository;

import com.example.cinematicketbookingapp.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Long> {

    @Query("SELECT seat from Seat seat where seat.screeningRoom.id=" +
            "(SELECT distinct screening.screeningRoom.id FROM Screening screening where screening.id=:screeningId)" +
            "order by seat.rowNumber asc , seat.seatNumber asc ")
    List<Seat> findAllSeatsOfScreeningRoomByScreeningIdOrderedByRowNumberAndSeatNumberAscending(Long screeningId);

}
