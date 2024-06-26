package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.converters;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.UserDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.UserRoles;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.stream.Collectors;

@Component
public class UserDTOToUserConverter implements Converter<UserDTO, User> {
    private final ConversionService conversionService;

    @Lazy
    public UserDTOToUserConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
    @Override
    public User convert(UserDTO source) {
        UserRoles userRole = UserRoles.valueOf(source.getRole().toUpperCase());

        return User.builder()
                .id(source.getId())
                .name(source.getName())
                .lastName(source.getLastName())
                .email(source.getEmail())
                .userRole(userRole)
                .bookings((source.getBookings()!=null ?source.getBookings() : Collections.emptyList()).stream()
                        .map(bookingDto -> conversionService.convert(bookingDto, Booking.class))
                        .toList())
                .build();
    }
}
