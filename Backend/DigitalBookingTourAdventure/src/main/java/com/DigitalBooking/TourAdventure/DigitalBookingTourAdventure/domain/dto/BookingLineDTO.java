package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Tour;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BookingLineDTO {

    private Long id;

    private Integer quantity;

    private LocalDate date;

    private BookingDTO bookingDTO;

    private TourDTO tourDTO;

}
