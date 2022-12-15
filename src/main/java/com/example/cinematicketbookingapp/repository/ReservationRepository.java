package com.example.cinematicketbookingapp.repository;

import com.example.cinematicketbookingapp.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation,Long> {

    List<Reservation> findAllByScreeningId(Long screeningId);
}
