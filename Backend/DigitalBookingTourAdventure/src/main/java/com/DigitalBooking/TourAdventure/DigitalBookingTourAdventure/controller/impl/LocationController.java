package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.ILocationController;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LocationDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.BadIdRequestException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.LocationNameAlreadyExistsException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.LocationServiceImpl;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.TourCategoryServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/location")
public class LocationController implements ILocationController<LocationDTO> {
    private final LocationServiceImpl locationServiceImpl;
    @Autowired
    public LocationController(LocationServiceImpl locationServiceImpl){
        this.locationServiceImpl = locationServiceImpl;
    }
    @PostMapping
    public ResponseEntity<?> postLocation(LocationDTO locationDTO) throws LocationNameAlreadyExistsException {
        return ResponseEntity.ok(locationServiceImpl.postLocation(locationDTO));
    }

    @GetMapping
    public ResponseEntity<?> getAllLocation() {
        return ResponseEntity.ok(locationServiceImpl.getAllLocation());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(Long id) throws BadIdRequestException {
        ResponseEntity resp = null;
        Optional<LocationDTO> locationDTOOpt = locationServiceImpl.getLocationById(id);

        if(id != null && locationDTOOpt.isPresent()){
            resp = ResponseEntity.ok(locationDTOOpt.get());
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("Not found  Id: " + id);
        }
        return resp;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLocationById(Long id) throws BadIdRequestException {
        ResponseEntity resp = null;
        if(id != null && locationServiceImpl.getLocationById(id).isPresent()){
            locationServiceImpl.deleteLocationById(id);
            resp = ResponseEntity.ok("Removed location with Id: " + id);
        }else{
            resp = ResponseEntity.status(HttpStatus.NOT_FOUND).body("The location was not deleted because it was not found with Id: " + id);
        }
        return resp;
    }
}
