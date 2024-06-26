package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.*;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.ParseException;
import java.time.LocalDate;

public interface ITourController<T>{
    ResponseEntity<?> postTour(@RequestBody T t) throws TourNameAlreadyExistsException, BadNameRequestException, BadIdRequestException, CategoryTitleNotFoundException, ParseException;

    ResponseEntity<?> postTourWithImage(@RequestParam("dataTour") String dataTour, @RequestParam("imageMain") MultipartFile imageMain, @RequestParam("imageFiles") MultipartFile[] imageFiles);

    ResponseEntity<?> getAllTour();

    ResponseEntity<Page<TourDTO>> getAllPageTour(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    ResponseEntity<Page<TourDTO>> getTourByCategory(
            @PathVariable("category") String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) throws CategoryTitleNotFoundException;

    ResponseEntity<?> getRandomTours(@PathVariable("numberOfTours") int numberOfTours);

    ResponseEntity<?> getTourById(@PathVariable("id") Long id) throws BadIdRequestException;

    ResponseEntity<Page<TourDTO>> getTourByName(
            @PathVariable("name") String name,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    );

    ResponseEntity<?> getToursByAvailabilityBetween(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    ResponseEntity<?> deleteTourById(@PathVariable("id") Long id) throws BadIdRequestException;
}
