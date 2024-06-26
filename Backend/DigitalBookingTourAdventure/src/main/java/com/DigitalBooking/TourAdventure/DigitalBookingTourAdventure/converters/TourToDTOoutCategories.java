package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LocationDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourImageDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;
@Component
public class TourToDTOoutCategories implements Converter<Tour, TourDTO> {
    private final ConversionService conversionService;

    @Lazy
    public TourToDTOoutCategories(ConversionService conversionService) {
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
