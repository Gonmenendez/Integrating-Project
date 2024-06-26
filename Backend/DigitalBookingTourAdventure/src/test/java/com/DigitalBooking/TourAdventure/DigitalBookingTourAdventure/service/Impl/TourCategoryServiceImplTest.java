package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadNameRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TourCategoryServiceImplTest {
    @Autowired
    private TourCategoryServiceImpl tourCategoryServiceImpl;
    private TourCategoryDTO tourCategoryDTO;

    @BeforeEach
    public void setUp() {
        Random random = new Random();
        int randomNumber = random.nextInt(100);
        tourCategoryDTO = new TourCategoryDTO();
        tourCategoryDTO.setTitle("test " + randomNumber);
        tourCategoryDTO.setDescription("test de categorias");
    }

    @Test
    void postCategory() throws CategoryTitleNotFoundException {
        TourCategoryDTO createdTourCategory = tourCategoryServiceImpl.postCategory(tourCategoryDTO);
        assertNotNull(createdTourCategory.getId());
    }

    @Test
    void getAllCategory() {
        List<TourCategoryDTO> tourCategory = tourCategoryServiceImpl.getAllCategory();
        assertTrue(tourCategory.size() > 0);
    }

    @Test
    void getCategoryById() throws CategoryTitleNotFoundException, BadIdRequestException {
        TourCategoryDTO createdTourCategory = tourCategoryServiceImpl.postCategory(tourCategoryDTO);
        Optional<TourCategoryDTO> retrievedTourCategory = tourCategoryServiceImpl.getCategoryById(createdTourCategory.getId());

        assertTrue(retrievedTourCategory.isPresent());
        assertEquals(retrievedTourCategory.get().getTitle(), tourCategoryDTO.getTitle());
    }

    @Test
    void getCategoryByName() throws CategoryTitleNotFoundException, BadNameRequestException, CategoryTitleNotFoundException {
        TourCategoryDTO createdTourCategory = tourCategoryServiceImpl.postCategory(tourCategoryDTO);
        Optional<TourCategoryDTO> retrievedTourCategory = tourCategoryServiceImpl.getCategoryByTitle(createdTourCategory.getTitle());

        assertNotNull(retrievedTourCategory);
    }


    @Test
    void deleteCategoryById() throws CategoryTitleNotFoundException, BadIdRequestException {
        TourCategoryDTO createdTourCategory = tourCategoryServiceImpl.postCategory(tourCategoryDTO);
        tourCategoryServiceImpl.deleteCategoryById(createdTourCategory.getId());

        assertEquals(tourCategoryServiceImpl.getCategoryById(createdTourCategory.getId()), Optional.empty());
    }
}