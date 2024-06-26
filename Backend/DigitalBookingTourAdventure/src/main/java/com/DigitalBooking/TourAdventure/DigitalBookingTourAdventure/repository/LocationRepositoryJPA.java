package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Location;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepositoryJPA extends JpaRepository<Location,Long> {
    @Query("SELECT t FROM Location t WHERE t.name = ?1")
    Optional<Location> findLocationByName(String name);
}
