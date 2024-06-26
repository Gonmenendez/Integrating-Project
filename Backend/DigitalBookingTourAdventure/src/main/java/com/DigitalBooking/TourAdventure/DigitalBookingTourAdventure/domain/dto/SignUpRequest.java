package com.DigitalBooking.TourAdventure.DigitalBookingTourAdventure.domain.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignUpRequest {

    @NotEmpty(message = "Name is required")
    @Length(min = 3, max = 15, message = "Name must be between 4 and 50 characters")
    @Pattern(regexp = "^[^0-9]+$", message = "Name cannot contain numbers")
    private String name;

    @NotEmpty(message = "Last name is required")
    @Length(min = 3, max = 15, message = "Last name must be between 4 and 50 characters")
    @Pattern(regexp = "^[^0-9]+$", message = "Last name cannot contain numbers")
    private String lastName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotEmpty(message = "Password is required")
    @Length(min = 6, max = 20, message = "Password must be between 8 and 50 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase letter, one uppercase letter, and one special character")
    private String password;
}
