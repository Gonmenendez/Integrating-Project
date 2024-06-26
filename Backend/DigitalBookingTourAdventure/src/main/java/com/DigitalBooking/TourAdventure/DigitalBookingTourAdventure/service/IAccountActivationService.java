package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.VerificationToken;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.InvalidTokenException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TokenExpiredException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.WebRequest;

import java.util.Locale;

public interface IAccountActivationService {

    void createVerificationToken(UserDetails userDetails, String token);
    VerificationToken getVerificationToken(String VerificationToken);

    String registrationConfirm(String token, Locale locale) throws InvalidTokenException, TokenExpiredException;

    void expiredAccount(UserDetails userDetails);

    void verifyTokenValidityAndExpiration(VerificationToken verificationToken, UserDetails userDetails) throws InvalidTokenException, TokenExpiredException;

    String resendConfirmationEmail(WebRequest request, String email);

}
