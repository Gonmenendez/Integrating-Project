package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingLineNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TourAvailabilityException;
import com.amazonaws.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

public interface IBookingController<T> {

    ResponseEntity<?> postBooking(@RequestBody T t);
    ResponseEntity<?> getAllBooking();
    ResponseEntity<?> getBookingById(@PathVariable("id")  Long id) throws BookingNotFoundException;
    ResponseEntity<?> deleteBookingById(@PathVariable("id")  Long id) throws BookingNotFoundException;
    ResponseEntity<?> getBookingByUserName(@PathVariable("name")  String userName);
    ResponseEntity<?> updateBookingById(@PathVariable("id")  Long id, @RequestBody BookingDTO t) throws BookingNotFoundException;
    ResponseEntity<?> deleteBookingLine(@PathVariable("id") Long id,@PathVariable("lineId") Long lineId) throws BookingNotFoundException, BadIdRequestException, TourAvailabilityException, BookingLineNotFoundException;
}
