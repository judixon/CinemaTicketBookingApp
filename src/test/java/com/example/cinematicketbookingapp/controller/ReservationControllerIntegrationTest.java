package com.example.cinematicketbookingapp.controller;

import com.example.cinematicketbookingapp.config.AppFunctionalValuesConstants;
import com.example.cinematicketbookingapp.dto.ReservationCreationDataDto;
import com.example.cinematicketbookingapp.dto.ReservationSummaryDto;
import com.example.cinematicketbookingapp.model.Reservation;
import com.example.cinematicketbookingapp.model.Screening;
import com.example.cinematicketbookingapp.repository.ReservationRepository;
import com.example.cinematicketbookingapp.repository.ScreeningRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
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
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Autowired
    private ScreeningRepository screeningRepository;

    @Test
    void webApplicationAndServletContextShouldBeLoadedProperlyForTests() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertThat(servletContext).isNotNull();
        assertTrue(servletContext instanceof MockServletContext);
        assertThat(webApplicationContext.getBean(ControllerTestConstantValues.RESERVATION_CONTROLLER_BEAN_NAME)).isNotNull();
    }

    @Test
    void createReservation_shouldOccurOptimisticLock_whenAtLeastTwoThreadsWantToReserveAtLeastOneTheSameSeatAtTheSameTime() throws JsonProcessingException, InterruptedException {
        //given
        ReservationCreationDataDto reservationCreationDataDto1 = ReservationCreationDataDto.builder()
                .seatIds(Set.of(1L, 2L, 3L))
                .screeningId(1L)
                .numberOfAdultTickets(3)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();
        ReservationCreationDataDto reservationCreationDataDto2 = ReservationCreationDataDto.builder()
                .seatIds(Set.of(5L, 6L, 7L))
                .screeningId(1L)
                .numberOfAdultTickets(3)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody1 = objectMapper.writeValueAsString(reservationCreationDataDto1);
        String requestBody2 = objectMapper.writeValueAsString(reservationCreationDataDto2);

        MockHttpServletRequestBuilder requestWithOptimisticLock = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody1);
        MockHttpServletRequestBuilder requestWithoutOptimisticLock = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
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
        assertThat(result).hasSize(2);
    }

    @Test
    void createReservation_shouldReturn404StatusWithProperMessage_whenScreeningWithRequestedIdDoesNotExistInTheDatabase() throws Exception {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(1L, 2L, 3L))
                .screeningId(100L)
                .numberOfAdultTickets(3)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String handledExceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();

        //then
        assertThat(handledExceptionMessage).isEqualTo(ControllerTestConstantValues.SCREENING_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    void createReservation_shouldReturn404StatusWithProperMessage_whenSeatWithRequestedIdDoesNotExistInTheDatabase() throws Exception {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(9999L))
                .screeningId(1L)
                .numberOfAdultTickets(1)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String handledExceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();

        //then
        assertThat(handledExceptionMessage).isEqualTo(ControllerTestConstantValues.SEAT_NOT_FOUND_EXCEPTION_MESSAGE);
    }

    @Test
    void createReservation_shouldReturn400StatusWithProperMessage_whenAtLeastOneOfRequestedSeatsIsAlreadyReserved() throws Exception {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(1L))
                .screeningId(1L)
                .numberOfAdultTickets(1)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        mockMvc.perform(request); //reserving the seat
        MvcResult result = mockMvc.perform(request) //reserving again the same seat
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();
        String handledExceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();

        //then
        assertThat(handledExceptionMessage).isEqualTo(ControllerTestConstantValues.SEAT_ALREADY_RESERVED_EXCEPTION_MESSAGE);
    }

    @Test
    void createReservation_shouldReturn400StatusWithProperMessage_whenTicketsNumberIsNotEqualToChosenSeatsNumber() throws Exception {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(10L))
                .screeningId(1L)
                .numberOfAdultTickets(2)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();
        String handledExceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();

        //then
        assertThat(handledExceptionMessage).isEqualTo(ControllerTestConstantValues.RESERVED_SEAT_NUMBER_UNEQUAL_TO_TICKET_NUMBER_MESSAGE);
    }

    @Test
    void createReservation_shouldReturn400StatusWithProperMessage_whenAfterReservationThereWouldBeSingleUnreservedSeatLeft() throws Exception {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(9L))
                .screeningId(1L)
                .numberOfAdultTickets(1)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();
        String handledExceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();

        //then
        assertThat(handledExceptionMessage).isEqualTo(ControllerTestConstantValues.SINGLE_UNRESERVED_SEAT_LEFT_EXCEPTION);
    }

    @Test
    void createReservation_shouldReturn400StatusWithProperMessage_whenScreeningSeatsReservationSystemIsClosedRightBeforeTheScreening() throws Exception {
        //given
        Screening screening = Screening.builder()
                .startDateTime(LocalDateTime.now().plusMinutes(AppFunctionalValuesConstants.BEFORE_SCREENING_RESERVATION_CREATING_BLOCKING_TIME.getMinute() - 10))
                .build();

        screeningRepository.save(screening);

        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(5L))
                .screeningId(31L)
                .numberOfAdultTickets(1)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(400))
                .andReturn();
        String handledExceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();

        //then
        assertThat(handledExceptionMessage).isEqualTo(ControllerTestConstantValues.RESERVATION_SYSTEM_CLOSED_EXCEPTION_MESSAGE);
    }

    @Test
    void createReservation_shouldReturn201StatusWithProperResponseBody_whenRequestBodyDataIsValidAndCompliesWithRequirements() throws Exception {
        //given
        ReservationCreationDataDto reservationCreationDataDto = ReservationCreationDataDto.builder()
                .seatIds(Set.of(14L, 15L, 16L))
                .screeningId(1L)
                .numberOfStudentTickets(1)
                .numberOfAdultTickets(1)
                .numberOfChildTickets(1)
                .ownerName(ControllerTestConstantValues.CORRECT_USER_NAME)
                .ownerSurname(ControllerTestConstantValues.CORRECT_USER_SURNAME)
                .build();

        String requestBody = objectMapper.writeValueAsString(reservationCreationDataDto);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.post(ControllerTestConstantValues.RESERVATIONS_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody);

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(201))
                .andReturn();
        ReservationSummaryDto requestResult = objectMapper.readValue(result.getResponse().getContentAsString(), ReservationSummaryDto.class);

        //then
        assertThat(requestResult.reservationId()).isEqualTo(1);
        assertThat(requestResult.totalPrice()).isEqualTo(BigDecimal.valueOf(55.5));
        assertThat(requestResult.expirationTime().getDayOfYear())
                .isEqualTo(LocalDateTime.now().plusDays(AppFunctionalValuesConstants.RESERVATION_EXPIRATION_TIME.getDays()).getDayOfYear());
    }
}