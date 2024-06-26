package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.CreationBookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TourAvailabilityException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.UserNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IBookingService {

    BookingDTO create(CreationBookingDTO dto) throws BookingNotFoundException, TourAvailabilityException, UserNotFoundException;

    BookingDTO update(Long id, BookingDTO dto) throws BookingNotFoundException, TourAvailabilityException;

    void delete(Long id) throws BookingNotFoundException;

    BookingDTO getById(Long id) throws BookingNotFoundException;

    List<BookingDTO> getAllBooking() throws BookingNotFoundException;

    Page<BookingDTO> getPurchaseOrders(int page, int size);
}
