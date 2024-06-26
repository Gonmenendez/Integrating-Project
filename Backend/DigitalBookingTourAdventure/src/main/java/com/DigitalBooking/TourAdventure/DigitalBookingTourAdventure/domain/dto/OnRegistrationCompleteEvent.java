package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import org.springframework.context.ApplicationEvent;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Locale;

public class OnRegistrationCompleteEvent extends ApplicationEvent {

    private String appUrl;
    private Locale locale;
    private UserDetails userDetails;

    public OnRegistrationCompleteEvent(
            UserDetails userDetails, Locale locale, String appUrl) {
        super(userDetails);

        this.userDetails = userDetails;
        this.locale = locale;
        this.appUrl = appUrl;
    }

    public OnRegistrationCompleteEvent(
            UserDetails userDetails) {
        super(userDetails);

        this.userDetails = userDetails;

    }

    public String getAppUrl() {
        return appUrl;
    }

    public void setAppUrl(String appUrl) {
        this.appUrl = appUrl;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
