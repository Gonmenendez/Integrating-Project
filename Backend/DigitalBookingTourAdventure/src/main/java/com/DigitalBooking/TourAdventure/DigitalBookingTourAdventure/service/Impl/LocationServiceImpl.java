package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LocationDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Location;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadNameRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.LocationNameAlreadyExistsException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.LocationRepositoryJPA;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.ILocationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LocationServiceImpl implements ILocationService {
    private final LocationRepositoryJPA locationRepositoryJPA;
    private final ModelMapper modelMapper;
    @Autowired
    public LocationServiceImpl(LocationRepositoryJPA locationRepositoryJPA, ModelMapper modelMapper) {
        this.locationRepositoryJPA = locationRepositoryJPA;
        this.modelMapper = modelMapper;
    }
    @Override
    public List<LocationDTO> getAllLocation() {
        List<LocationDTO> locationsDTO = new ArrayList<>();
        List<Location> locations = locationRepositoryJPA.findAll();

        for (Location location: locations){
            LocationDTO locationDTO = modelMapper.map(location, LocationDTO.class);
            locationsDTO.add(locationDTO);
        }
        return locationsDTO;
    }

    @Override
    public Optional<LocationDTO> getLocationById(Long id) throws BadIdRequestException {
        if(id <= 0 || id == null){
            throw new BadIdRequestException("Invalid id: " + id);
        }
        Optional<LocationDTO> locationDTOOpt = null;
        Optional<Location> location = locationRepositoryJPA.findById(id);
        if (location.isPresent()){
            LocationDTO locationDTO = modelMapper.map(location.get(), LocationDTO.class);
            locationDTOOpt = Optional.of(locationDTO);
        }else{
            locationDTOOpt = Optional.empty();
        }
        return locationDTOOpt;
    }

    @Override
    public Optional<LocationDTO> getLocationByName(String name) throws BadNameRequestException {
        if(name.isEmpty()){
            throw new BadNameRequestException("Invalid name: " + name);
        }
        Optional<LocationDTO> locationDTOOpt = null;
        Optional<Location> location = locationRepositoryJPA.findLocationByName(name);
        if (location.isPresent()){
            LocationDTO locationDTO = modelMapper.map(location.get(), LocationDTO.class);
            locationDTOOpt = Optional.of(locationDTO);
        }else{
            locationDTOOpt = Optional.empty();
        }
        return locationDTOOpt;
    }

    @Override
    public LocationDTO postLocation(LocationDTO locationDTO) throws LocationNameAlreadyExistsException {
        Location location = modelMapper.map(locationDTO, Location.class);
        if(locationRepositoryJPA.findLocationByName(location.getName()).isPresent()){
            throw new LocationNameAlreadyExistsException("Location name already exists: " + location.getName());
        }
        location = locationRepositoryJPA.save(location);
        locationDTO = modelMapper.map(location, LocationDTO.class);
        return locationDTO;
    }

    @Override
    public void deleteLocationById(Long id) throws BadIdRequestException {
        if(id<=0 || id == null){
            throw new BadIdRequestException("Invalid id: " + id);
        }
        locationRepositoryJPA.deleteById(id);
    }
}
