package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.UserDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class TourDTOtoEntity implements Converter<TourDTO, Tour> {

    private final ConversionService conversionService;

    @Lazy
    public TourDTOtoEntity(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public Tour convert(TourDTO source) {

        return Tour.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .shortDescription(source.getShortDescription())
                .price(source.getPrice())
                .score(source.getScore())
                .duration(source.getDuration())
                .startTime(source.getStartTime())
                .categories((source.getCategories()!=null ? source.getCategories(): Collections.emptyList()).stream()
                        .map(categories -> conversionService.convert(categories, TourCategory.class))
                        .collect(Collectors.toSet()))
                .locations((source.getLocations()!=null ? source.getLocations(): Collections.emptyList()).stream()
                        .map(location -> conversionService.convert(location, Location.class))
                        .collect(Collectors.toSet()))
                .images((source.getImages()!=null ? source.getImages(): Collections.emptyList()).stream()
                        .map(tourImage -> conversionService.convert(tourImage, TourImage.class))
                        .collect(Collectors.toSet()))
                .quantity(source.getQuantity())
                .availability(source.getAvailability())
                .bookingLines((source.getBookingLinesDTO()!=null ? source.getBookingLinesDTO(): Collections.emptyList()).stream()
                        .map(bookingLineDTO -> conversionService.convert(bookingLineDTO, BookingLine.class))
                        .collect(Collectors.toSet()))
                .build();
    }
}
