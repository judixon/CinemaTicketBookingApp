package com.example.cinematicketbookingapp.mapper;

import com.example.cinematicketbookingapp.dto.SeatListDto;
import com.example.cinematicketbookingapp.model.Seat;
import org.springframework.stereotype.Service;

@Service
public class SeatDtoMapper {

   public SeatListDto mapToSeatListDto(Seat seat){
        return SeatListDto.builder()
                .seatId(seat.getId())
                .seatNumber(seat.getSeatNumber())
                .rowNumber(seat.getRowNumber())
                .screeningRoomId(seat.getScreeningRoom().getId())
                .build();
    }
}
