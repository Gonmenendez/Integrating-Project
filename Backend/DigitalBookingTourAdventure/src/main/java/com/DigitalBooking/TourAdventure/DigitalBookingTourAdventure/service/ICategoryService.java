package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadNameRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleAlreadyExistException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleNotFoundException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ICategoryService {
    List<TourCategoryDTO> getAllCategory();
    Optional<TourCategoryDTO> getCategoryById(Long id) throws BadIdRequestException;
    Optional<TourCategoryDTO> getCategoryByTitle(String name) throws BadNameRequestException, CategoryTitleNotFoundException;
    TourCategoryDTO postCategory(TourCategoryDTO tourCategoryDTO) throws CategoryTitleNotFoundException;
    TourCategoryDTO postCategoryWithImage(TourCategoryDTO tourCategoryDTO, MultipartFile imageFile) throws CategoryTitleNotFoundException, CategoryTitleAlreadyExistException;
    void deleteCategoryById(Long id) throws BadIdRequestException;

}
