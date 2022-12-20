package com.example.cinematicketbookingapp.controller;

import com.example.cinematicketbookingapp.dto.ScreeningDetailsDto;
import com.example.cinematicketbookingapp.dto.ScreeningListDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletContext;
import java.util.Comparator;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ScreeningControllerIntegrationTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Test
    public void WebApplicationAndServletContextShouldBeLoadedProperlyForTests() {
        ServletContext servletContext = webApplicationContext.getServletContext();

        assertThat(servletContext).isNotNull();
        assertTrue(servletContext instanceof MockServletContext);
        assertThat(webApplicationContext.getBean(ControllerTestConstantValues.SCREENING_CONTROLLER_BEAN_NAME)).isNotNull();
    }

    @Test
    void getScreenings_shouldReturnStatus200AndAllScreenings_whenScreeningStartDateTimesAreInGivenTimePeriod() throws Exception {
        //given
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(ControllerTestConstantValues.SCREENINGS_PATH)
                .param(ControllerTestConstantValues.FROM_DATE_TIME_PARAM, "2023-01-01T00:00:00")
                .param(ControllerTestConstantValues.TO_DATE_TIME_PARAM, "2023-10-01T00:00:00");

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ScreeningListDto[] requestResult = objectMapper.readValue(result.getResponse().getContentAsString(), ScreeningListDto[].class);
        System.out.println(requestResult[1]);

        //then
        assertThat(requestResult).hasSize(30);
        assertThat(requestResult).isSortedAccordingTo(Comparator.comparing(ScreeningListDto::screeningStartDateTime));
    }

    @Test
    void getScreenings_shouldReturnStatus200AndNoScreenings_whenScreeningStartDateTimesAreNotInGivenTimePeriod() throws Exception {
        //given
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(ControllerTestConstantValues.SCREENINGS_PATH)
                .param(ControllerTestConstantValues.FROM_DATE_TIME_PARAM, "2023-09-01T00:00:00")
                .param(ControllerTestConstantValues.TO_DATE_TIME_PARAM, "2023-10-01T00:00:00");

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ScreeningListDto[] requestResult = objectMapper.readValue(result.getResponse().getContentAsString(), ScreeningListDto[].class);

        //then
        assertThat(requestResult.length).isEqualTo(0);
    }

    @Test
    void getScreening_shouldReturnStatus200AndRequestedScreenings_whenScreeningWithParticularIdExistsInDatabase() throws Exception {
        //given
        long screeningId = 1L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(ControllerTestConstantValues.SCREENINGS_PATH + "/" + screeningId);


        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        ScreeningDetailsDto requestResult = objectMapper.readValue(result.getResponse().getContentAsString(), ScreeningDetailsDto.class);

        //then
        assertThat(requestResult).isNotNull();
        assertThat(requestResult.screeningId()).isEqualTo(1);
    }

    @Test
    void getScreening_shouldReturnStatus404AndProperExceptionMessage_whenScreeningWithRequestedIdDoesNotExistInDatabase() throws Exception {
        //given
        long screeningId = 100L;
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get(ControllerTestConstantValues.SCREENINGS_PATH + "/" + screeningId);

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(404))
                .andReturn();
        String handledExceptionMessage = Objects.requireNonNull(result.getResolvedException()).getMessage();

        //then
        assertThat(handledExceptionMessage).isEqualTo(ControllerTestConstantValues.SCREENING_NOT_FOUND_EXCEPTION_MESSAGE);
    }
}