package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.jwt;

import jakarta.servlet.http.HttpServletRequest;

public interface IBearerTokenResolver {

    String resolve(HttpServletRequest request);
}
