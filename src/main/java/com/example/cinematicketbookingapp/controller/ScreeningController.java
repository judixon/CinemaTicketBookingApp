package com.example.cinematicketbookingapp.controller;

import com.example.cinematicketbookingapp.dto.ScreeningDetailsDto;
import com.example.cinematicketbookingapp.dto.ScreeningListDto;
import com.example.cinematicketbookingapp.service.ScreeningService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/screenings")
public class ScreeningController {

    private final ScreeningService screeningService;

    @GetMapping
    ResponseEntity<List<ScreeningListDto>> getScreenings(@RequestParam @ApiParam(example = "2023-01-01T00:00:00") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDateTime,
                                                         @RequestParam @ApiParam(example = "2023-10-01T00:00:00") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDateTime,
                                                         @RequestParam(defaultValue = "startDateTime") String firstSortParam,
                                                         @RequestParam(defaultValue = "movie.title") String secondSortParam,
                                                         @RequestParam(defaultValue = "ASC") Sort.Direction firstParamSortDirection,
                                                         @RequestParam(defaultValue = "ASC") Sort.Direction secondParamSortDirection) {

        return ResponseEntity.ok(screeningService.getScreenings(firstSortParam, secondSortParam, firstParamSortDirection, secondParamSortDirection,
                fromDateTime, toDateTime));
    }

    @GetMapping("/{id}")
    ResponseEntity<ScreeningDetailsDto> getScreening(@PathVariable Long id) {
        return ResponseEntity.ok(screeningService.getScreening(id));
    }
}
