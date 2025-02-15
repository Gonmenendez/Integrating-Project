package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.jwt;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtUtil implements IJwtUtil{


    @Override
    public String extractUserName(String token,String secretKey) {
        Claims claims = extractAllClaims(token,secretKey);
        return claims.getSubject();
    }

    @Override
    public String generateToken(UserDetails userDetails,
                                long systemCurrentMillis,
                                long configuredExpirationTimeInMillis,
                                String secretKey) {

        Map<String, Object> claims = new HashMap<>();

        claims.put("name", ((User) userDetails).getName());
        claims.put("lastName", ((User) userDetails).getLastName());
        claims.put("rol", ((User) userDetails).getUserRole());

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(systemCurrentMillis))
                .setExpiration(new Date(systemCurrentMillis + configuredExpirationTimeInMillis))
                .signWith(getKey(secretKey), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public Date extractExpiration(String token,String secretKey) {
        Claims claims = extractAllClaims(token,secretKey);
        return claims.getExpiration();
    }

    private Key getKey(String secretKey) {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }
    private Claims extractAllClaims(String token,String secretKey) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey(secretKey))
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
