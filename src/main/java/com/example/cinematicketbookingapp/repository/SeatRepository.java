package com.example.cinematicketbookingapp.repository;

import com.example.cinematicketbookingapp.model.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat,Long> {

   List<Seat> findAllByScreeningRoomId(Long screeningRoomId);

}
