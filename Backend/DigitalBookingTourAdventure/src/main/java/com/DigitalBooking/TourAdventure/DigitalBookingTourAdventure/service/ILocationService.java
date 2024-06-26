package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LocationDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadNameRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.LocationNameAlreadyExistsException;

import java.util.List;
import java.util.Optional;

public interface ILocationService {
    List<LocationDTO> getAllLocation();
    Optional<LocationDTO> getLocationById(Long id) throws BadIdRequestException;
    Optional<LocationDTO> getLocationByName(String name) throws BadNameRequestException;
    LocationDTO postLocation(LocationDTO locationDTO) throws LocationNameAlreadyExistsException;
    void deleteLocationById(Long id) throws BadIdRequestException;
}
