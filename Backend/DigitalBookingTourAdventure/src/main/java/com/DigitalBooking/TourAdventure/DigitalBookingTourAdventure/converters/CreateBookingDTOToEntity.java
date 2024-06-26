package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.CreationBookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
@Component
public class CreateBookingDTOToEntity implements Converter<CreationBookingDTO, Booking> {

    private final ConversionService conversionService;

    @Lazy
    public CreateBookingDTOToEntity(ConversionService conversionService) {
        this.conversionService = conversionService;
    }


    @Override
    public Booking convert(CreationBookingDTO source) {

        return Booking.builder()
                .bookingLines((source.getBookingLineDTOS()!=null?source.getBookingLineDTOS(): Collections.emptyList()).stream()
                        .map(bookingLines -> conversionService.convert(bookingLines, BookingLine.class))
                        .toList())
                .user(source.getUser() != null ? conversionService.convert(source.getUser(), User.class) : null)
                .totalPrice(source.getTotalPrice())
                .status(true)
                .deleted(false)
                .build();
    }
}