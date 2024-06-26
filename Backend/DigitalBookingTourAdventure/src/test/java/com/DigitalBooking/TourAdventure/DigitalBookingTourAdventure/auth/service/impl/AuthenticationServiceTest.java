package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.auth.service.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.AuthResponse;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LoginRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.SignUpRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.User;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.models.UserRoles;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.jwt.JwtService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.AuthenticationService;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.Impl.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    private static final String JWT_EXAMPLE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJudWxsIiwiaWF0IjoxNjg1MDQ3NzY0LCJleHAiOjE2ODUwNDg5NjR9.fgSG8gKJvGzt0Weq1n_049w4Dxakc7XBm-R8oqRLTpI";
    @Mock
    AuthenticationManager authenticationManager;
    @Mock
    UserService userService;
    @Mock
    JwtService jwtService;
    @Mock
    WebRequest webRequest;

    @InjectMocks
    AuthenticationService authenticationService;

    @Test
    void login() {
        User existingUser = User.builder().id(1L).email("pedro@gmail.com").password("asdDas276%").userRole(UserRoles.USER).build();
        Authentication authenticationMock = Mockito.mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authenticationMock);
        when(authenticationMock.getPrincipal()).thenReturn(existingUser);
        when(jwtService.generateToken(existingUser)).thenReturn(JWT_EXAMPLE);

        AuthResponse authenticationResponse =
                authenticationService.login(LoginRequest.builder().email("testuser@gmail.com").password("213hUwo#").build());

        assertNotNull(authenticationResponse);
        assertEquals(JWT_EXAMPLE,authenticationResponse.getJwtToken());
    }

//    @Test
//    void signUp() {
//        User newUser = User.builder().id(1L).name("pedro").lastName("Lalala").email("heuhi@odne.com").password("asdDas276%").userRole(UserRoles.USER).build();
//        when(userService.signUp(any())).thenReturn(newUser);
//        when(jwtService.generateToken(newUser)).thenReturn(JWT_EXAMPLE);
//
//        AuthResponse authenticationResponse =
//                authenticationService.signUp(SignUpRequest.builder().name("TestUser").lastName("UserTest").email("testuser@usertest.com").password("213hUwo#").build(), webRequest);
//
//        assertNotNull(authenticationResponse);
//        assertEquals(JWT_EXAMPLE,authenticationResponse.getJwtToken());
//    }

}