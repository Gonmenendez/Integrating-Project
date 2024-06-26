package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.OnRegistrationCompleteEvent;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.VerificationToken;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.InvalidTokenException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TokenExpiredException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.VerificationTokenRepository;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IAccountActivationService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IUserService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Locale;

@Service
@Transactional
@AllArgsConstructor
public class AccountActivationService implements IAccountActivationService {

    private final VerificationTokenRepository tokenRepository;

    private final IUserService userService;

    private final ApplicationEventPublisher eventPublisher;

    @Override
    public void createVerificationToken(UserDetails userDetails, String token) {
        VerificationToken myToken = new VerificationToken(token, userDetails);
        tokenRepository.save(myToken);
    }

    @Override
    public VerificationToken getVerificationToken(String VerificationToken) {
        return tokenRepository.findByToken(VerificationToken);
    }

    @Override
    public void verifyTokenValidityAndExpiration(VerificationToken verificationToken, UserDetails userDetails) throws InvalidTokenException, TokenExpiredException {

        if (verificationToken == null) {
            throw new InvalidTokenException("Invalid token!");
        }

        LocalDateTime expiryDate = verificationToken.getExpiryDate();
        LocalDateTime currentTime = LocalDateTime.now();
        if (expiryDate.isBefore(currentTime)) {
            this.expiredAccount(userDetails);
            throw new TokenExpiredException("The token has expired! You need to register again.");
        }

    }

    @Override
    public String registrationConfirm(String token, Locale locale) throws InvalidTokenException, TokenExpiredException {
        VerificationToken verificationToken = this.getVerificationToken(token);

        UserDetails userDetails = verificationToken.getUser();

        this.verifyTokenValidityAndExpiration(verificationToken, userDetails);

        userService.registrationConfirm(userDetails);
        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDetails));

        return "Account Confirmed!";
    }

    @Override
    public void expiredAccount(UserDetails userDetails){

        userService.setAccountExpired(userDetails);

    }

    @Override
    public String resendConfirmationEmail(WebRequest request, String email){
        UserDetails userDetails = userService.getUserByEmail(email);

        eventPublisher.publishEvent(new OnRegistrationCompleteEvent(userDetails, request.getLocale(), request.getContextPath()));

        return "Confirmation Email Sent!";

    }

}
