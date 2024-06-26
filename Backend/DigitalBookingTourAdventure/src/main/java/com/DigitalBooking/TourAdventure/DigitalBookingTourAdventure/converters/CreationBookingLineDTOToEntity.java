package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.CreationBookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.CreationBookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class CreationBookingLineDTOToEntity implements Converter<CreationBookingLineDTO, BookingLine> {
    private final ConversionService conversionService;
    @Lazy
    public CreationBookingLineDTOToEntity(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public BookingLine convert(CreationBookingLineDTO source) {
        return BookingLine.builder()
                .quantity(source.getQuantity())
                .date(source.getDate())
                .booking(source.getBookingDTO() != null ? conversionService.convert(source.getBookingDTO(), Booking.class) : null)
                .tour(source.getTourDTO() != null ? conversionService.convert(source.getTourDTO(), Tour.class) : null)
                .build();
    }
}

