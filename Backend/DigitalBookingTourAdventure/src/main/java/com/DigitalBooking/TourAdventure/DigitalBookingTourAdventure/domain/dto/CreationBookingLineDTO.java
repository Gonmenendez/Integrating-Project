package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreationBookingLineDTO {
    private Integer quantity;

    private LocalDate date;

    private BookingDTO bookingDTO;

    private TourDTO tourDTO;

    public TourDTO getTourDTO() {
        return tourDTO;
    }
}
