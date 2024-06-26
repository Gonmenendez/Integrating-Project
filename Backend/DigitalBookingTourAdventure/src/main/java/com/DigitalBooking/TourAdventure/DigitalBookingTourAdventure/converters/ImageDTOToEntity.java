package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourImageDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourImage;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageDTOToEntity implements Converter< TourImageDTO, TourImage> {

    private final ConversionService conversionService;

    @Lazy
    public ImageDTOToEntity(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Override
    public TourImage convert(TourImageDTO value) {
        return TourImage.builder()
                .id(value.getId())
                .urlImage(value.getUrlImage())
                .tour(value.getTour() != null ? conversionService.convert(value.getTour(), Tour.class) : null)
                .isPrincipal(value.getIsPrincipal())
                .build();
    }
}
