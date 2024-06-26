package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.BookingLine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingLineRepositoryJPA extends JpaRepository<BookingLine, Long> {
}
