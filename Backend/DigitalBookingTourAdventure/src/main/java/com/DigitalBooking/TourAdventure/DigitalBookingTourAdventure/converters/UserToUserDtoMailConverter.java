package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.UserDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoMailConverter implements Converter<User, UserDTO> {
    private final ConversionService conversionService;
    @Lazy
    public UserToUserDtoMailConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public UserDTO convert(User source) {
        UserDTO userDTO = new UserDTO();
        userDTO.setName(source.getName());
        userDTO.setLastName(source.getLastName());
        userDTO.setEmail(source.getEmail());
        return userDTO;
    }
}
