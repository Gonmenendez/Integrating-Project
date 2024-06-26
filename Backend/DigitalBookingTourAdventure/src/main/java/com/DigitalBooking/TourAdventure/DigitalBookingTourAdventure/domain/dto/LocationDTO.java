package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class LocationDTO {

    private Long id;
    private String name;
    private String description;
    private String address;

    private Set<TourDTO> tours;

    public LocationDTO() {
        this.tours = new HashSet<>();
    }

    public void addTour(TourDTO tour) {
        this.tours.add(tour);
        tour.getLocations().add(this);
    }

    public LocationDTO(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }
}
