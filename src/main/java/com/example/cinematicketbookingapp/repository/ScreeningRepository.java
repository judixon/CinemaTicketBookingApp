package com.example.cinematicketbookingapp.repository;

import com.example.cinematicketbookingapp.model.Screening;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening,Long> {
}
