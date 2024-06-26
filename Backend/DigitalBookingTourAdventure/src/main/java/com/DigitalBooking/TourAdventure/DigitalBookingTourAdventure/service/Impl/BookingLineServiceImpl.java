package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingLineNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BookingNotFoundException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.BookingLineRepositoryJPA;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IBookingLineService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingLineServiceImpl implements IBookingLineService {
    private final BookingLineRepositoryJPA bookingLineRepositoryJPA;

    private final ConversionService conversionService;

    public BookingLineServiceImpl(BookingLineRepositoryJPA bookingLineRepositoryJPA, ConversionService conversionService) {
        this.bookingLineRepositoryJPA = bookingLineRepositoryJPA;
        this.conversionService = conversionService;
    }

    @Override
    public List<BookingLineDTO> getAllBookingLine() {
        var bookingsLine = bookingLineRepositoryJPA.findAll();
        return bookingsLine.stream()
                .map(booking -> conversionService.convert(bookingsLine, BookingLineDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public BookingLineDTO getBookingLineById(Long id) throws BookingLineNotFoundException {
        var bookingLine = bookingLineRepositoryJPA.findById(id);
        if(bookingLine.isPresent()){
            return conversionService.convert(bookingLine.get(),BookingLineDTO.class);
        }else{
            throw new BookingLineNotFoundException("BookingLine not found!");
        }
    }

    @Override
    public Optional<BookingLineDTO> getBookingLineByQuantity(Integer quantity) {
        return Optional.empty();
    }

    @Override
    public Optional<BookingLineDTO> getBookingLineByDate(LocalDate date) {
        return Optional.empty();
    }

    @Override
    public BookingLineDTO create(BookingLineDTO bookingLineDTO) {
        return null;
    }

    @Override
    public void delete(Long id) throws BadIdRequestException {
        if(id <= 0 || id == null){
            throw new BadIdRequestException("Invalid id: " + id);
        }
        bookingLineRepositoryJPA.deleteById(id);
    }
}
