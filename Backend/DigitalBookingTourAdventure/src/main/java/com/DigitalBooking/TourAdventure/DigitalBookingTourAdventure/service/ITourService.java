package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.*;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ITourService {
    List<TourDTO> getAllTour();
    Page<TourDTO> getAllPageTour(int page, int size);
    Optional<TourDTO> getTourById(Long id) throws BadIdRequestException;
    Page<TourDTO> getToursByName(String name, int page, int size);
    Page<TourDTO> getToursByCategory(String category, int page, int size) throws CategoryTitleNotFoundException;
    List<TourDTO> getRandomTours(int numberOfTours);
    TourDTO postTour(TourDTO tourDTO) throws TourNameAlreadyExistsException, BadNameRequestException, BadIdRequestException, CategoryTitleNotFoundException, ParseException;
    TourDTO postTourWithImagen(TourDTO tourDTO, MultipartFile imageMain, MultipartFile[] imageFiles) throws TourNameAlreadyExistsException, CategoryTitleNotFoundException, BadIdRequestException, TourHasImagesException;
    void deleteTourById(Long id) throws BadIdRequestException;

    void updateTourLessAvailability(Tour tourUpdateAvailability, LocalDate date, Integer people) throws TourAvailabilityException;

    void updateTourAdditionAvailability(Tour tourUpdateAvailability, LocalDate date, Integer people) throws TourAvailabilityException;

    List<TourDTO> getToursByAvailability(LocalDate startDate, LocalDate endDate);
}
