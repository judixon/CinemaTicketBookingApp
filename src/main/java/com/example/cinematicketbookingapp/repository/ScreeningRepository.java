package com.example.cinematicketbookingapp.repository;

import com.example.cinematicketbookingapp.model.Screening;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ScreeningRepository extends JpaRepository<Screening, Long> {

    List<Screening> findAllByStartDateTimeAfterAndStartDateTimeBefore(Sort sort, LocalDateTime fromDateTome, LocalDateTime toDateTime);
}
