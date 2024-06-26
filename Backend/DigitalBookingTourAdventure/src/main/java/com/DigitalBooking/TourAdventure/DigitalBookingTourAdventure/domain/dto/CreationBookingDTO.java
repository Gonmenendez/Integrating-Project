package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreationBookingDTO {
    private UserDTO user;
    private List<BookingLineDTO> bookingLineDTOS;
    private BigDecimal totalPrice;

}
