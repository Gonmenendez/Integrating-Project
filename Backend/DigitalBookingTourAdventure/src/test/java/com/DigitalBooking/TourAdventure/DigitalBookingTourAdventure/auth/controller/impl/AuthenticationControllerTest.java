package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.auth.controller.impl;

import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.controller.impl.AuthenticationController;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.AuthResponse;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.LoginRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto.SignUpRequest;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.jwt.JwtRequestFilter;
import com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.service.IAuthenticationService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.context.request.WebRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AuthenticationController.class)
class AuthenticationControllerTest {

    public static final String JWT_EXAMPLE = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY4NTA0NzczMCwiZXhwIjoxNjg1MDQ4OTMwfQ.woVZr6L87DOlwg_qf_viNdMSgLwhItdEjhbXJQkILK0";

    @MockBean
    JwtRequestFilter jwtRequestFilter;

    @MockBean
    IAuthenticationService authenticationService;

    @MockBean
    WebRequest webRequest;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    AuthenticationController authenticationController;

    @Test
    void login() throws Exception {
        ArgumentCaptor<LoginRequest> argumentCaptor = ArgumentCaptor.forClass(LoginRequest.class);
        AuthResponse authenticationResponse = AuthResponse.builder()
                .jwtToken(JWT_EXAMPLE).build();
        when(authenticationService.login(argumentCaptor.capture())).thenReturn(authenticationResponse);
        mockMvc.perform(MockMvcRequestBuilders.post("/api/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "email":"equipo3c1pi@gmail.com",
                                    "password":"adminPass$3"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.jwtToken").value(JWT_EXAMPLE));

        verify(authenticationService).login(any());

        assertEquals("equipo3c1pi@gmail.com", argumentCaptor.getValue().getEmail());
        assertEquals("adminPass$3", argumentCaptor.getValue().getPassword());
    }

    @Test
    void signUp() throws Exception {
        ArgumentCaptor<SignUpRequest> argumentCaptor = ArgumentCaptor.forClass(SignUpRequest.class);
        AuthResponse authenticationResponse = AuthResponse.builder()
                .jwtToken(JWT_EXAMPLE).build();
        when(authenticationService.signUp(argumentCaptor.capture(), any(WebRequest.class))).thenReturn(authenticationResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sign-up")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "name": "AdminTest",
                                    "lastName": "TestAdmin",
                                    "email": "equipo3c1pi@gmail.com",
                                    "password":"adminPass$3"
                                }
                                """))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.jwtToken").value(JWT_EXAMPLE));

        verify(authenticationService).signUp(any(SignUpRequest.class), any(WebRequest.class));

        assertEquals("AdminTest", argumentCaptor.getValue().getName());
        assertEquals("TestAdmin", argumentCaptor.getValue().getLastName());
        assertEquals("equipo3c1pi@gmail.com", argumentCaptor.getValue().getEmail());
        assertEquals("adminPass$3", argumentCaptor.getValue().getPassword());
    }

}