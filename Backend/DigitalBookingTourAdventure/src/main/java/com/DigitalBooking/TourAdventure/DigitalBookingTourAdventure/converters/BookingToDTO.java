package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.UserDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
@Component
public class BookingToDTO implements Converter<Booking, BookingDTO> {

    private final ConversionService conversionService;

    @Lazy
    public BookingToDTO(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BookingDTO convert(Booking source) {

        return BookingDTO.builder()
                .bookingLineDTOS((source.getBookingLines()!=null?source.getBookingLines(): Collections.emptyList()).stream()
                        .map(bookingLines -> conversionService.convert(bookingLines, BookingLineDTO.class))
                        .toList())
                .id(source.getId())
                .user(source.getUser() != null ? conversionService.convert(source.getUser(), UserDTO.class) : null)
                .status(source.getStatus())
                .totalPrice(source.getTotalPrice())
                .build();
    }
}
