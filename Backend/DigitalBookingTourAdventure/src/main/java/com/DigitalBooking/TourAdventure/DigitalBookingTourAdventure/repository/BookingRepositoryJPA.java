package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BookingRepositoryJPA extends JpaRepository<Booking, Long> {

    @Query("SELECT COUNT(bo) > 0 FROM Booking bo WHERE bo.id = :id and bo.deleted = false")
    boolean exists(@Param("id") Long id);

    @Query("update Booking bo set bo.deleted=true where bo.id=?1 and bo.deleted = false")
    @Modifying
    int softDelete(Long id);

    Optional<Booking> findByIdAndDeleted(Long id, Boolean deleted);

    Page<Booking> findByDeleted(Boolean deleted, Pageable pageable);

    @Query("SELECT t FROM Booking t WHERE t.user.email = ?1")
    List<Booking> findByUser(String userName);

}
