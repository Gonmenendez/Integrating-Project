package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
@Component
public class BookingLineToDTO implements Converter<BookingLine, BookingLineDTO> {

    private final ConversionService conversionService;

    @Lazy
    public BookingLineToDTO(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BookingLineDTO convert(BookingLine source) {
        return BookingLineDTO.builder()
                .id(source.getId())
                .quantity(source.getQuantity())
                .tourDTO(source.getTour() != null ? conversionService.convert(source.getTour(), TourDTO.class) : null)
                .date(source.getDate())
                .build();
    }
}
