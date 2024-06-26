package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.TourCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TourCategoryRepositoryJPA extends JpaRepository<TourCategory,Long> {
    @Query("SELECT t FROM TourCategory t WHERE t.title = ?1")
    Optional<TourCategory> findByCategoryTitle(String name);
}
