package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.TourCategoryServiceImpl;
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
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TourCategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TourCategoryServiceImpl tourCategoryService;

    @BeforeEach
    void setUp() throws BadIdRequestException {
        TourCategoryDTO tourCategoryDTO = new TourCategoryDTO();
        tourCategoryDTO.setId(1L);
        tourCategoryDTO.setTitle("Tour Category Test");
        when(tourCategoryService.getCategoryById(1L)).thenReturn(Optional.of(tourCategoryDTO));
        when(tourCategoryService.getAllCategory()).thenReturn(Arrays.asList(tourCategoryDTO));
    }

    @Test
    void postTourCategory() throws Exception {
        TourCategoryDTO tourCategoryDTO = new TourCategoryDTO();
        tourCategoryDTO.setTitle("Post Tour Category Test");
        tourCategoryDTO.setDescription("This is a test Tour Category");

        TourCategoryDTO expectedResponseDTO = new TourCategoryDTO();
        expectedResponseDTO.setId(1L);
        expectedResponseDTO.setTitle("Post Tour Category Test");
        expectedResponseDTO.setDescription("This is a test Tour Category");

        when(tourCategoryService.postCategory(tourCategoryDTO)).thenReturn(expectedResponseDTO);

        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(tourCategoryDTO);
        String expectedResponseJson = objectMapper.writeValueAsString(expectedResponseDTO);

        MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/tourCategory")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        String actualResponseJson = result.getResponse().getContentAsString();
        Assertions.assertEquals(expectedResponseJson, actualResponseJson);
;
    }

    @Test
    void getAllTourCategory() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tourCategory"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.length()").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].title").value("Tour Category Test"));
    }

    @Test
    void getTourCategoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tourCategory/1"))
                .andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("Tour Category Test"))
                .andExpect(content().json("{\"id\": 1, \"title\": \"Tour Category Test\"}"));
    }

    @Test
    void deleteTourCategoryById() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.delete("/tourCategory/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Removed tourCategory with Id: 1"));
    }
}