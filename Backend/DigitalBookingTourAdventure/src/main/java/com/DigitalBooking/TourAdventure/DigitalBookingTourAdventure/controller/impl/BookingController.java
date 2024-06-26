package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.IBookingController;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.CreationBookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingLineNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TourAvailabilityException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.BookingServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("booking")
public class BookingController implements IBookingController<CreationBookingDTO> {

    private final BookingServiceImpl bookingService;

    private final ConversionService conversionService;

    @Autowired
    public BookingController(BookingServiceImpl bookingService, ConversionService conversionService) {
        this.bookingService = bookingService;
        this.conversionService = conversionService;
    }

    @Override
    @PostMapping
    public ResponseEntity<?> postBooking(CreationBookingDTO creationBookingDTO) {
        ResponseEntity resp = null;
        try {
            resp = ResponseEntity.ok(bookingService.create(creationBookingDTO));
        } catch (Exception e) {
            String errorMessage = "Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
        }
        return resp;
    }

    @GetMapping
    public ResponseEntity<?> getAllBooking() {
        return ResponseEntity.ok(bookingService.getAllBooking());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(Long id) throws BookingNotFoundException {
        ResponseEntity resp = null;
        Optional<BookingDTO> bookingDTOOptional = Optional.ofNullable(bookingService.getById(id));
        if (id != null && bookingDTOOptional.isPresent()) {
            resp = ResponseEntity.ok(bookingDTOOptional.get());
        } else {
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found  Id: " + id);
        }
        return resp;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBookingById(Long id) throws BookingNotFoundException {
        ResponseEntity resp = null;
        Optional<BookingDTO> bookingDTOOptional = Optional.ofNullable(bookingService.getById(id));
        if (id != null && bookingDTOOptional.isPresent()) {
            bookingService.delete(id);
            resp = ResponseEntity.ok("Removed Booking with Id: " + id);
        } else {
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("The Booking was not deleted because it was not found with Id: " + id);
        }
        return resp;
    }

    @DeleteMapping("/{id}/line/{lineId}")
    public ResponseEntity<?> deleteBookingLine(@PathVariable("id")Long id,@PathVariable("lineId")Long lineId) throws BookingNotFoundException, BadIdRequestException, TourAvailabilityException, BookingLineNotFoundException {
        ResponseEntity resp = null;
        Optional<BookingDTO> bookingDTOOptional = Optional.ofNullable(bookingService.getById(id));
        if (id != null && bookingDTOOptional.isPresent()) {
            bookingService.deleteBookingLine(id, lineId);
            resp = ResponseEntity.ok("Removed Booking with Id: " + id);
        } else {
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("The Booking was not deleted because it was not found with Id: " + id);
        }
        return resp;
    }

    @GetMapping("/user/{userEmail}")
    public ResponseEntity<?> getBookingByUserName(@PathVariable("userEmail") String userEmail) {
        try {
            List<BookingDTO> bookings = bookingService.getBookingsByUserName(userEmail);
            if (!bookings.isEmpty()) {
                return ResponseEntity.ok(bookings);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No bookings found for user: " + userEmail);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while fetching bookings.");
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateBookingById(Long id, BookingDTO bookingDTO) throws BookingNotFoundException {
        try {
            BookingDTO bookingExistDto = bookingService.getById(id);
            if (bookingExistDto != null) {
                BookingDTO bookingDTO1 = conversionService.convert(bookingDTO, BookingDTO.class);
                bookingService.update(id, bookingDTO1);
                return ResponseEntity.ok("Updated Booking with Id: " + id);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The Booking was not updated because it was not found with Id: " + id);
            }
        } catch (BookingNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred during booking update.");
        }
    }
}
