package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "locations")
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private String address;

    @ManyToMany(mappedBy = "locations")
    private Set<Tour> tours;

    public Location() {
        this.tours = new HashSet<>();
    }

    public void addTour(Tour tour) {
        this.tours.add(tour);
        tour.getLocations().add(this);
    }

    public Location(String name, String description, String address) {
        this.name = name;
        this.description = description;
        this.address = address;
    }
}
