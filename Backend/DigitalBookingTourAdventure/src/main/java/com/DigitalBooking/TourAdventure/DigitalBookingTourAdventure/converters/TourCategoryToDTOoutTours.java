package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourCategoryDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourCategory;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class TourCategoryToDTOoutTours implements Converter<TourCategory, TourCategoryDTO> {

    private final ConversionService conversionService;

    @Lazy
    public TourCategoryToDTOoutTours(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public TourCategoryDTO convert(TourCategory source) {
        return TourCategoryDTO.builder()
                .id(source.getId())
                .title(source.getTitle())
                .description(source.getDescription())
                .urlImage(source.getUrlImage())
                .build();


    }
}
