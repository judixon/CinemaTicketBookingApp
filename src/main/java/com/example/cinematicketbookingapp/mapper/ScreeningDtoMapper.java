package com.example.cinematicketbookingapp.mapper;

import com.example.cinematicketbookingapp.dto.ScreeningDetailsDto;
import com.example.cinematicketbookingapp.dto.ScreeningListDto;
import com.example.cinematicketbookingapp.model.Screening;
import org.springframework.stereotype.Service;

@Service
public class ScreeningDtoMapper {
    public ScreeningListDto mapToScreeningListDto(Screening screening) {
        return ScreeningListDto.builder()
                .screeningId(screening.getId())
                .movieTitle(screening.getMovie().getTitle())
                .screeningStartDateTime(screening.getStartDateTime())
                .build();
    }

    public ScreeningDetailsDto mapToScreeningDetailsDto(Screening screening) {
        return ScreeningDetailsDto.builder()
                .screeningId(screening.getId())
                .movieTitle(screening.getMovie().getTitle())
                .screeningStartDateTime(screening.getStartDateTime())
                .screeningEndDateTime(screening.getEndDateTime())
                .build();
    }
}
