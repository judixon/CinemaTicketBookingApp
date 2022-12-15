package com.example.cinematicketbookingapp.mapper;

import com.example.cinematicketbookingapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SeatsAvailabilitySchemaMapper {

    private final SeatRepository seatRepository;

//    SeatsAvailabilitySchemaDto createAvailableSeatsSchema(Screening screening){
//        List<Seat> allScreeningRoomSeats = seatRepository.findAllByScreeningRoomId(screening.getScreeningRoom().getId());
//        List<Seat> alreadyReservedSeats =
//    }
}
