package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TourImageRepositoryJPA extends JpaRepository<TourImage, Long> {
    @Query("SELECT t FROM TourImage t WHERE t.urlImage = ?1")
    Optional<TourImage> findByUrlImage(String urlImage);
    @Query("SELECT t FROM TourImage t WHERE t.tour.id = ?1")
    List<TourImage> findByTourId(Long tourId);
}
