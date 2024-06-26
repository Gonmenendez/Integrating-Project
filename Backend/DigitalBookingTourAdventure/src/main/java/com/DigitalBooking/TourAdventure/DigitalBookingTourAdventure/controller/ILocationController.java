package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.LocationNameAlreadyExistsException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface ILocationController<T>{
    ResponseEntity<?> postLocation(@RequestBody T t) throws LocationNameAlreadyExistsException;
    ResponseEntity<?> getAllLocation();
    ResponseEntity<?> getLocationById(@PathVariable("id") Long id) throws BadIdRequestException;
    ResponseEntity<?> deleteLocationById(@PathVariable("id") Long id) throws BadIdRequestException;
}
