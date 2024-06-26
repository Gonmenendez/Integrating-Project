package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.AuthResponse;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LoginRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.SignUpRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.InvalidTokenException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TokenExpiredException;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

public interface IAuthenticationService {

    AuthResponse login(LoginRequest loginRequest);
    AuthResponse signUp(SignUpRequest signUpRequest, WebRequest request);
}
