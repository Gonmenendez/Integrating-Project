package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.CategoryTitleNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

public interface ITourCategoryController<T> {
    ResponseEntity<?> postTourCategory(@RequestBody T t) throws CategoryTitleNotFoundException;
    ResponseEntity<?> postTourCategoryWithImage(@RequestParam("data") String data, @RequestParam("imageFile") MultipartFile imageFile);
    ResponseEntity<?> getAllTourCategory();
    ResponseEntity<?> getTourCategoryById(@PathVariable("id") Long id) throws BadIdRequestException;
    ResponseEntity<?> deleteTourCategoryById(@PathVariable("id") Long id) throws BadIdRequestException;

}
