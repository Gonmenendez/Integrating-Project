package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.OnRegistrationCompleteEvent;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.VerificationToken;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.exceptions.TokenExpiredException;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IAccountActivationService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IMailService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IUserService;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.MessageSource;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Component
@AllArgsConstructor
public class RegistrationListener implements
        ApplicationListener<OnRegistrationCompleteEvent> {

    private final IUserService userService;

    private final IMailService mailService;

    private final MessageSource messageSource;

    private final IAccountActivationService accountActivationService;


    @Override
    public void onApplicationEvent(OnRegistrationCompleteEvent event) {

        if (event.getUserDetails().isEnabled()) {
            this.accountIsConfirmed(event);
        } else{
            try {
                this.confirmRegistration(event);
            } catch (TokenExpiredException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void confirmRegistration(OnRegistrationCompleteEvent event) throws TokenExpiredException {

        User user = (User) event.getUserDetails();
        String token;

        if (user.getVerificationToken() != null) {
            VerificationToken verificationToken = user.getVerificationToken();

            LocalDateTime expiryDate = verificationToken.getExpiryDate();
            LocalDateTime currentTime = LocalDateTime.now();
            if (expiryDate.isBefore(currentTime)) {
                accountActivationService.expiredAccount(event.getUserDetails());
                throw new TokenExpiredException("The token has expired!");
            }

            token = verificationToken.getToken();

        } else{
            UserDetails userDetails = event.getUserDetails();
            token = UUID.randomUUID().toString();
            accountActivationService.createVerificationToken(userDetails, token);
        }


        this.sendConfirmationEmail(user, token, event.getAppUrl());
    }

    private void sendConfirmationEmail(User user, String token, String appUrl){

        String recipientAddress = user.getEmail();
        String subject = messageSource.getMessage("email.confirmation.subject", null, null);
        String confirmationUrl
                = appUrl + "/api/registrationConfirm?token=" + token;
        String message = messageSource.getMessage("email.confirmation.body", new Object[]{user.getName(), user.getLastName(), confirmationUrl, subject}, null);


        mailService.sendMail(recipientAddress, subject, message);

    }

    private void accountIsConfirmed(OnRegistrationCompleteEvent event){
        UserDetails userDetails = event.getUserDetails();
        User user = (User) userDetails;

        String recipientAddress = userDetails.getUsername();
        String subject = messageSource.getMessage("email.enabledaccount.subject", null, null);
        String message = messageSource.getMessage("email.enabledaccount.body", new Object[]{user.getName(), user.getLastName(), user.getEmail()}, null);

        mailService.sendMail(recipientAddress, subject, message);

    }
}
