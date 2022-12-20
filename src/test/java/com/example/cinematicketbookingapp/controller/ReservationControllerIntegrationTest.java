package com.example.cinematicketbookingapp.controller;

import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.model.Reservation;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReservationControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void webApplicationAndServletContextShouldBeLoadedProperlyForTests() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertThat(servletContext).isNotNull();
        assertTrue(servletContext instanceof MockServletContext);
        assertThat(webApplicationContext.getBean("screeningController")).isNotNull();
    }

    @Test
    void createReservation_shouldOccurOptimisticLock_whenAtLeastTwoThreadsWantToReserveAtLeastOneTheSameSeatAtTheSameTime() throws JsonProcessingException, InterruptedException {
        //given
        ReservationCreationDataDto reservationCreationDataDto1 = ReservationCreationDataDto.builder()
                .seatIds(Set.of(1L, 2L, 3L))
                .screeningId(1L)
                .numberOfAdultTickets(3)
                .ownerName("Name")
                .ownerSurname("Two-Part")
                .build();
        ReservationCreationDataDto reservationCreationDataDto2 = ReservationCreationDataDto.builder()
                .seatIds(Set.of(5L, 6L, 7L))
                .screeningId(1L)
                .numberOfAdultTickets(3)
                .ownerName("Name")
                .ownerSurname("Two-Part")
                .build();

        String requestBody1 = objectMapper.writeValueAsString(reservationCreationDataDto1);
        String requestBody2 = objectMapper.writeValueAsString(reservationCreationDataDto2);

        MockHttpServletRequestBuilder requestWithOptimisticLock = MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1);
        MockHttpServletRequestBuilder requestWithoutOptimisticLock = MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody2);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        Callable callableWithOptimisticLock = () -> mockMvc.perform(requestWithOptimisticLock);
        Callable callableWithoutOptimisticLock = () -> mockMvc.perform(requestWithoutOptimisticLock);

        //when
        executorService.submit(callableWithOptimisticLock); //reservation saved
        executorService.submit(callableWithOptimisticLock); //lock
        executorService.submit(callableWithOptimisticLock); //lock
        executorService.submit(callableWithoutOptimisticLock); //reservation saved (requested different seats)

        Thread.sleep(1000); //time for requests to be executed

        List<Reservation> result = reservationRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(2);
    }

    @Test
    void createReservation_shouldReturn404StatusWithProperMessage_whenScreeningWithRequestedIdDoesNotExistInTheDatabase() throws JsonProcessingException {

    }

    @Test
    void createReservation_shouldReturn404StatusWithProperMessage_whenSeatWithRequestedIdDoesNotExistInTheDatabase(){

    }

    @Test
    void createReservation_shouldReturn400StatusWithProperMessage_whenAtLeastOneOfRequestedSeatsIsAlreadyReserved(){

    }

    @Test
    void createReservation_shouldReturn400StatusWithProperMessage_whenTicketsNumberIsNotEqualToChosenSeatsNumber(){

    }

    @Test
    void createReservation_shouldReturn400StatusWithProperMessage_whenAfterReservationThereWouldBeSingleUnreservedSeatLeft(){

    }

    @Test
    void createReservation_shouldReturn201StatusWithProperResponseBody_whenRequestBodyDataIsValidAndCompliesWithRequirements(){

    }
}