package com.example.cinematicketbookingapp.controller;

import com.example.cinematicketbookingapp.model.Screening;
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
        assertThat(webApplicationContext.getBean("screeningController")).isNotNull();
    }

    @Test
    void shouldReturnSelectedScreenings() throws Exception {
        //given
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders.get("/screenings")
                .param("fromDateTime", "2023-01-01T00:00:00")
                .param("toDateTime", "2023-10-01T00:00:00");

        //when
        MvcResult result = mockMvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().is(200))
                .andReturn();
        Screening[] requestResult = objectMapper.readValue(result.getResponse().getContentAsString(), Screening[].class);

        //then
        assertThat(requestResult.length).isEqualTo(30);
    }
}