package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.*;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.*;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;
@Component
public class TourToDTO implements Converter<Tour, TourDTO> {

    private final ConversionService conversionService;

    @Lazy
    public TourToDTO(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public TourDTO convert(Tour source) {

        return TourDTO.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .shortDescription(source.getShortDescription())
                .price(source.getPrice())
                .score(source.getScore())
                .duration(source.getDuration())
                .startTime(source.getStartTime())
                .categories((source.getCategories() != null ? source.getCategories() : Collections.emptyList()).stream()
                        .map(categories -> conversionService.convert(categories, TourCategoryDTO.class))
                        .collect(Collectors.toSet()))
                .locations((source.getLocations() != null ? source.getLocations() : Collections.emptyList()).stream()
                        .map(location -> conversionService.convert(location, LocationDTO.class))
                        .collect(Collectors.toSet()))
                .images((source.getImages() != null ? source.getImages() : Collections.emptyList()).stream()
                        .map(tourImage -> conversionService.convert(tourImage, TourImageDTO.class))
                        .collect(Collectors.toSet()))
                .quantity(source.getQuantity())
                .availability(source.getAvailability())
                .build();
    }
}
