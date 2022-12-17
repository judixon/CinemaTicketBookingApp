package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.config.AppFunctionalValues;
import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.dto.ReservationSummaryDto;
import com.example.cinematicketbookingapp.mapper.ReservationDtoMapper;
import com.example.cinematicketbookingapp.model.Reservation;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationDtoMapper reservationDtoMapper;

    public ReservationSummaryDto createReservation(ReservationCreationDataDto reservationCreationDataDto) {
        Reservation reservation = reservationDtoMapper.mapToReservation(reservationCreationDataDto);
        //walidacja ownerName @AdnotacjaValidation
        //walidacja ownerSurname @AdnotacjaValidation
        //walidacja czy liczba biletów jest taka sama jak liczba zajętych miejsc EXCEPTION_1
        //walidacja czy czy wszystkie wybrane miejsca są wolne EXCEPTION_2
        //walidacja czy wśród wybranych miejsc nie ma pojedynczego wolnego pomiędzy EXCEPTION_3
        //walidacja czy wybrane miejsca nie zostawiają jednego wolnego po uwzględnieniu już zarezerwowanych (środek + brzegi) EXCEPTION_3
        //walidacja, czy liczba miejsc i liczba biletów jest taka sama
        //jedna transakcja na raz może działać żeby nie można było zrobić rezerwacji na to samo miejsce w tym samym czasie dwukrotnie
        //utworzenie rezerwacji
        //obliczenie total prize

        return reservationDtoMapper.mapToReservationSummaryDto(reservationRepository.save(reservation)).toBuilder()
                .totalPrice(countTotalPrize(reservationCreationDataDto))
                .build();
    }

    private BigDecimal countTotalPrize(ReservationCreationDataDto dto) {
        return AppFunctionalValues.ADULT_TICKET_PRICE.multiply(BigDecimal.valueOf(dto.amountOfAdultTickets())
                        .add(AppFunctionalValues.STUDENT_TICKET_PRICE.multiply(BigDecimal.valueOf(dto.amountOfStudentTickets()))))
                .add(AppFunctionalValues.CHILD_TICKET_PRICE.multiply(BigDecimal.valueOf(dto.amountOfChildTickets())));
    }
}
