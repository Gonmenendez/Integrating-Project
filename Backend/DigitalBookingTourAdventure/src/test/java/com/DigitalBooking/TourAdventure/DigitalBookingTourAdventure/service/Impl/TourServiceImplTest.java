package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class TourServiceImplTest {
    @Autowired
    private TourServiceImpl tourServiceImpl;

    private TourDTO tourDTO;
    private TourCategoryDTO tourCategoryDTO;
    private TourDTO createdTour;

    @BeforeEach
    public void setUp() throws BadIdRequestException, TourNameAlreadyExistsException, BadNameRequestException, CategoryTitleNotFoundException, ParseException {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        tourDTO = new TourDTO();
        tourDTO.setName("test" + randomNumber);
        tourDTO.setDescription("tour lindo");
        tourDTO.setPrice(20000.0);
        tourDTO.setScore(4.0);
        tourCategoryDTO = new TourCategoryDTO();
        tourCategoryDTO.setTitle("excursion");
        createdTour = tourServiceImpl.postTour(tourDTO);
    }

    @Test
    void postTour()  {
        assertNotNull(createdTour.getId());
    }

    @Test
    void getAllTour() {
        List<TourDTO> tours = tourServiceImpl.getAllTour();
        assertTrue(tours.size() > 0);
    }

    @Test
    void getTourById() throws BadIdRequestException {
        Optional<TourDTO> retrievedTour = tourServiceImpl.getTourById(createdTour.getId());

        assertTrue(retrievedTour.isPresent());
        assertEquals(retrievedTour.get().getName(), tourDTO.getName());
    }

    @Test
    void getTourByName() {
        Page<TourDTO> tour = tourServiceImpl.getToursByName(createdTour.getName(), 0, 10);
        assertNotNull(tour);
    }

    @Test
    void deleteTourById() throws BadIdRequestException {
        tourServiceImpl.deleteTourById(createdTour.getId());
        assertEquals(tourServiceImpl.getTourById(createdTour.getId()), Optional.empty());
    }
}
