package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;


import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingLineNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface IBookingLineService {
    List<BookingLineDTO> getAllBookingLine();
    BookingLineDTO getBookingLineById(Long id) throws BookingLineNotFoundException;
    Optional<BookingLineDTO> getBookingLineByQuantity(Integer quantity);
    Optional<BookingLineDTO> getBookingLineByDate(LocalDate date);
    BookingLineDTO create(BookingLineDTO bookingLineDTO);
    void delete(Long id) throws BadIdRequestException;
}
