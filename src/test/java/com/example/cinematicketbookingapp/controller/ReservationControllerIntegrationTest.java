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
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;

import java.util.List;
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
    public void optimisticLockExceptionTests() throws JsonProcessingException, InterruptedException {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(1L,2L,3L))
                .screeningId(1L)
                .numberOfAdultTickets(3)
                .ownerName("Name")
                .ownerSurname("Two-Part")
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post("/reservations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        ExecutorService executorService = Executors.newFixedThreadPool(2);
        Callable callable1 = () -> mockMvc.perform(request);

        //when
        executorService.submit(callable1);
        executorService.submit(callable1);
        Thread.sleep(1000);

        List<Reservation> result = reservationRepository.findAll();

        //then
        assertThat(result.size()).isEqualTo(1);
    }
}