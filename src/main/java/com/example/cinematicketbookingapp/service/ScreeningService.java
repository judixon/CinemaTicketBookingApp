package com.example.cinematicketbookingapp.service;

import com.example.cinematicketbookingapp.dto.ScreeningDetailsDto;
import com.example.cinematicketbookingapp.dto.ScreeningListDto;
import com.example.cinematicketbookingapp.dto.SeatsAvailabilitySchemaDto;
import com.example.cinematicketbookingapp.exceptions.DefaultExceptionMessages;
import com.example.cinematicketbookingapp.exceptions.ResourceNotFoundException;
import com.example.cinematicketbookingapp.mapper.ScreeningDtoMapper;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import com.example.cinematicketbookingapp.repository.SeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ScreeningService {

    private final ScreeningRepository screeningRepository;
    private final SeatRepository seatRepository;
    private final ScreeningDtoMapper screeningDtoMapper;

    public List<ScreeningListDto> getScreenings(String firstSortingParam, String secondSortingParam,
                                                Sort.Direction firstParamDirection, Sort.Direction secondParamDirection) {
        return screeningRepository.findAll(Sort.by(firstParamDirection,firstSortingParam)
                        .and(Sort.by(secondParamDirection,secondSortingParam)))
                .stream()
                .map(screeningDtoMapper::mapToScreeningListDto)
                .toList();
//        return screeningRepository.findAll(Sort.by(Sort.Direction.valueOf(firstParamDirection),firstSortingParam)
//                        .and(Sort.by(Sort.Direction.valueOf((secondParamDirection),secondSortingParam))))
//                .stream()
//                .map(screeningDtoMapper::mapToScreeningListDto)
//                .toList();
    }

    public ScreeningDetailsDto getScreening(Long screeningId){
        return screeningRepository.findById(screeningId)
                .map(screeningDtoMapper::mapToScreeningDetailsDto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        String.format(DefaultExceptionMessages.SCREENING_NOT_FOUND_EXCEPTION_MESSAGE,screeningId)));
    }
}
