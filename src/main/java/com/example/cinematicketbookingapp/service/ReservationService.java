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
      //walidacja ownerName @AdnotacjaValidation
      //walidacja ownerSurname @AdnotacjaValidation
      //walidacja czy liczba biletów jest taka sama jak liczba zajętych miejsc EXCEPTION_1
      //walidacja czy czy wszystkie wybrane miejsca są wolne EXCEPTION_2
      //walidacja czy wśród wybranych miejsc nie ma pojedynczego wolnego pomiędzy EXCEPTION_3
      //walidacja czy wybrane miejsca nie zostawiają jednego wolnego po uwzględnieniu już zarezerwowanych (środek + brzegi) EXCEPTION_3
      //jedna transakcja na raz może działać żeby nie można było zrobić rezerwacji na to samo miejsce w tym samym czasie dwukrotnie
      //utworzenie rezerwacji
      //obliczenie total prize

      return null;
    }
}
