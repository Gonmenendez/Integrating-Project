package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.TourServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TourControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TourServiceImpl tourService;

    @BeforeEach
    void setUp() throws BadIdRequestException {
        TourDTO tourDTO = new TourDTO();
        tourDTO.setId(1L);
        tourDTO.setName("Tour Test");
        when(tourService.getTourById(1L)).thenReturn(Optional.of(tourDTO));
        when(tourService.getAllTour()).thenReturn(Arrays.asList(tourDTO));
    }

    @Test
    void testGetAllTour() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tour"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value("Tour Test"));
    }

    @Test
    void testGetTourById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tour/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value("Tour Test"))
                .andExpect(content().json("{\"id\": 1, \"name\": \"Tour Test\"}"));

    }

    @Test
    void testGetTourById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tour/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Not found Id: 2"));
    }

    @Test
    public void testPostTour() throws Exception {

        TourDTO tourDTO = new TourDTO();
        tourDTO.setName("Post Test Tour");
        tourDTO.setDescription("This is a test tour");

        TourDTO expectedResponseDTO = new TourDTO();
        expectedResponseDTO.setId(1L);
        expectedResponseDTO.setName("Post Test Tour");
        expectedResponseDTO.setDescription("This is a test tour");

        when(tourService.postTour(tourDTO)).thenReturn(expectedResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(tourDTO);
        String expectedResponseJson = objectMapper.writeValueAsString(expectedResponseDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tour")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String actualResponseJson = result.getResponse().getContentAsString();
        Assertions.assertEquals(expectedResponseJson, actualResponseJson);
    }

    @Test
    void testDeleteTourById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tour/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Removed tour with Id: 1"));
    }

    @Test
    void testDeleteTourById_NotFound() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tour/2"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("The tour was not deleted because it was not found with Id: 2"));
    }

}

