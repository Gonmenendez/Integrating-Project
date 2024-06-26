package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.auth.service.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.SignUpRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.UserRoles;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.repository.UserRepository;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.ErrorResponseException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    UserRepository userRepository;
    @Mock
    PasswordEncoder passwordEncoder;
    @Mock
    ConversionService conversionService;

    @InjectMocks
    UserService userService;

    @Test
    void loadUserByUsername() {
        User existingUser = User.builder().id(1L).email("maria@gmail.com").password("asDas@1").userRole(UserRoles.USER).isAccountVerified(true).isAccountExpired(false).build();
        when(userRepository.findByEmail(any())).thenReturn(Optional.ofNullable(existingUser));

        UserDetails userDetails = userService.loadUserByUsername("maria@gmail.com");

        assertNotNull(userDetails);
        assertEquals("maria@gmail.com", userDetails.getUsername());
    }

    @Test
    void loadUserByUsername_nonExistent_throwsException() {
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("maria"));
    }

    @Test
    void createUser() {
        User newUser = User.builder().id(1L).name("pedro").lastName("Lalala").email("heuhi@odne.com").password("dssdY$5").userRole(UserRoles.USER).build();
        when(userRepository.save(any())).thenReturn(newUser);

        UserDetails userDetails = userService.signUp(SignUpRequest.builder().name("pedro").lastName("Lalala").email("heuhi@odne.com").password("dssdY$5").build());

        verify(passwordEncoder).encode(any());
        assertNotNull(userDetails);
    }

    @Test
    void createUser_alreadyExisting_throwsException() {
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
        SignUpRequest signUpRequest = SignUpRequest.builder().name("pedro").lastName("Lalala").email("heuhi@odne.com").password("dssdY$5").build();

        assertThrows(ErrorResponseException.class, () ->
                userService.signUp(signUpRequest));
    }


}