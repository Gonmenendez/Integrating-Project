package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourImageDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourImage;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ImageToDTO implements Converter<TourImage, TourImageDTO> {
    private final ConversionService conversionService;

    @Lazy
    public ImageToDTO(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Override
    public TourImageDTO convert(TourImage value) {
        return TourImageDTO.builder()
                .id(value.getId())
                .urlImage(value.getUrlImage())
                .isPrincipal(value.getIsPrincipal())
                .build();
    }

}
