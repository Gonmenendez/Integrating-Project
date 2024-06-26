package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.OnRegistrationCompleteEvent;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.VerificationToken;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.InvalidTokenException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TokenExpiredException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.jwt.JwtService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.AuthResponse;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LoginRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.SignUpRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IAccountActivationService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IAuthenticationService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.util.Calendar;
import java.util.Locale;

@Service
@AllArgsConstructor
public class AuthenticationService implements IAuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final IUserService userService;
    private final JwtService jwtService;
    private final ApplicationEventPublisher eventPublisher;

    @Override
    public AuthResponse login(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(),
                        loginRequest.getPassword()));

        String jwt =jwtService
                .generateToken((UserDetails) authentication.getPrincipal());

        return AuthResponse.builder()
                .jwtToken(jwt)
                .build();
    }

    @Override
    public AuthResponse signUp(SignUpRequest signUpRequest, WebRequest request) {
        UserDetails userDetails = userService.signUp(signUpRequest);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDetails, request.getLocale(), request.getContextPath()));

        return AuthResponse.builder()
                .jwtToken(jwtService.generateToken(userDetails))
                .build();
    }



}
