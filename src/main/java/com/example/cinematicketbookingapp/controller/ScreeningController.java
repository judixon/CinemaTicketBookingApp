package com.example.cinematicketbookingapp.controller;

import com.example.cinematicketbookingapp.dto.ScreeningDetailsDto;
import com.example.cinematicketbookingapp.dto.ScreeningListDto;
import com.example.cinematicketbookingapp.service.ScreeningService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    @GetMapping
    ResponseEntity<List<ScreeningListDto>> getScreenings(@PathVariable(required = false) String firstSortParam,
                                                         @PathVariable(required = false) String secondSortParam,
                                                         @PathVariable(required = false) Sort.Direction firstParamSortDirection,
                                                         @PathVariable(required = false) Sort.Direction secondParamSortDirection
                                                         ){
        System.out.println(firstSortParam);
        System.out.println(secondSortParam);
        System.out.println(firstParamSortDirection);
        System.out.println(secondParamSortDirection);
        return ResponseEntity.ok(screeningService.getScreenings(firstSortParam,secondSortParam,firstParamSortDirection,secondParamSortDirection));
    }

    @GetMapping("/{id}")
    ResponseEntity<ScreeningDetailsDto> getScreening(@PathVariable Long id){
        return ResponseEntity.ok(screeningService.getScreening(id));
    }
}
