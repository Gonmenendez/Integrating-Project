package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
@Component
public class BookingToEntity implements Converter<BookingDTO, Booking> {

    private final ConversionService conversionService;

    @Lazy
    public BookingToEntity(ConversionService conversionService) {
        this.conversionService = conversionService;
    }


    @Override
    public Booking convert(BookingDTO source) {

        return Booking.builder()
                .bookingLines((source.getBookingLineDTOS()!=null?source.getBookingLineDTOS(): Collections.emptyList()).stream()
                        .map(bookingLines -> conversionService.convert(bookingLines, BookingLine.class))
                        .toList())
                .id(source.getId())
                .user(source.getUser() != null ? conversionService.convert(source.getUser(), User.class) : null)
                .totalPrice(source.getTotalPrice())
                .status(source.getStatus())
                .deleted(false)
                .build();
    }
}
