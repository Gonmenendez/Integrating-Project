package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.Booking;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {

    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String role;

    private List<BookingDTO> bookings;

}
