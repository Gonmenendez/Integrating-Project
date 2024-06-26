package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.jwt;

import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService implements IJwtService {

    private final JwtUtil jwtUtil;
    private final JwtConfig jwtConfig;
    private final Clock clock;

    @Override
    public String extractUserName(String token) {
        return jwtUtil.extractUserName(token, jwtConfig.secretKey());
    }

    @Override
    public String generateToken(UserDetails userDetails) {
        return jwtUtil.generateToken(userDetails,
                clock.millis(),
                jwtConfig.expiration(),
                jwtConfig.secretKey());
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails, String username) {
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return jwtUtil.extractExpiration(token, jwtConfig.secretKey()).before(new Date(clock.millis()));
    }
}
