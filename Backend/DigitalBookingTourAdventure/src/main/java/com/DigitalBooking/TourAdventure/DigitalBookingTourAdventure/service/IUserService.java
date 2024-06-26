package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.SignUpRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.UserDTO;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.VerificationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface IUserService {

    List<UserDTO> getAllUsers();

    UserDTO getUserById(Long id);

    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    UserDetails signUp(SignUpRequest signUpRequest);

    void registrationConfirm(UserDetails userDetails);

    void setAccountExpired(UserDetails userDetails);

    UserDetails getUserByEmail(String email);

    void deleteUserById(Long id);

    void addAuthorityToUser(Long id, String authority);

    void removeAuthorityFromUser(Long id, String authority);

}
