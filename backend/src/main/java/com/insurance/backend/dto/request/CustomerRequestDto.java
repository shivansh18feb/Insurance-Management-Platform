package com.insurance.backend.dto.request;

import com.insurance.backend.entity.CustomerStatus;
import com.insurance.backend.entity.Gender;
import jakarta.validation.constraints.*;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerRequestDto {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$",
            message = "First name can contain only letters, spaces, apostrophes and hyphens"
    )
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Pattern(
            regexp = "^[A-Za-z]+([ '-][A-Za-z]+)*$",
            message = "Last name can contain only letters, spaces, apostrophes and hyphens"
    )
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(
            regexp = "^[6-9]\\d{9}$",
            message = "Phone number must be a valid 10-digit Indian mobile number"
    )
    private String phoneNumber;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Gender is required")
    private Gender gender;

    @NotBlank(message = "Address is required")
    @Size(max = 255)
    private String address;

    @NotBlank(message = "City is required")
    @Size(max = 100)
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "City can contain only letters and spaces"
    )
    private String city;

    @NotBlank(message = "State is required")
    @Size(max = 100)
    @Pattern(
            regexp = "^[A-Za-z ]+$",
            message = "State can contain only letters and spaces"
    )
    private String state;

    @NotBlank(message = "Postal code is required")
    @Pattern(
            regexp = "^[1-9][0-9]{5}$",
            message = "Invalid PIN code"
    )
    private String postalCode;
}