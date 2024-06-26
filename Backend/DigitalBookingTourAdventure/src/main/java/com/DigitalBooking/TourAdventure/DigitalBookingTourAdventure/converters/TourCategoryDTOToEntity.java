package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.BookingLineDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourCategory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class TourCategoryDTOToEntity  implements Converter<TourCategoryDTO, TourCategory> {
    private final ConversionService conversionService;

    @Lazy
    public TourCategoryDTOToEntity(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Override
    public TourCategory convert(TourCategoryDTO source) {
        return TourCategory.builder()
                .id(source.getId())
                .title(source.getTitle())
                .description(source.getDescription())
                .urlImage(source.getUrlImage())
                .tours((source.getTours()!=null?source.getTours(): Collections.emptyList()).stream()
                        .map(tour -> conversionService.convert(tour, Tour.class))
                        .collect(Collectors.toSet()))
                .build();


    }
}
