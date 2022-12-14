package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.dto.ReservationSummaryDto;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;

  public   ReservationSummaryDto createReservation(ReservationCreationDataDto reservationCreationDataDto){
        return null;
    }
}
