package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.TourDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;


public interface TourRepositoryJPA extends JpaRepository<Tour, Long> {

    @Query("SELECT t FROM Tour t WHERE t.name = :name")
    Page<Tour> findByName(String name, Pageable pageable);

    @Query("SELECT t FROM Tour t JOIN t.categories c WHERE c.title = :categoryTitle")
    Page<Tour> findByCategoryTitle(String categoryTitle, Pageable pageable);

    @Query(value = "SELECT t.* FROM tours t INNER JOIN tour_availability a ON t.id = a.tour_id WHERE a.tour_date >= :startDate AND a.tour_date <= :endDate", nativeQuery = true)
    List<Tour> fintToursByAvailabilityBetween(LocalDate startDate, LocalDate endDate);

}
