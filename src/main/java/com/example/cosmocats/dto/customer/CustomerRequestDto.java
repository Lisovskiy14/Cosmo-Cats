package com.example.cosmocats.dto.customer;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class CustomerRequestDto {

    @NotBlank(message = "is required")
    @Size(min = 2, max = 20, message = "must be between 2 and 20 characters long")
    String firstName;

    @NotBlank(message = "is required")
    @Size(min = 2, max = 20, message = "must be between 2 and 20 characters long")
    String lastName;

    @NotBlank(message = "is required")
    @Email(message = "must be a valid email address")
    String email;

    @NotBlank(message = "is required")
    @Pattern(
            regexp = "^[0-9]{10}$",
            message = "must be a valid phone number"
    )
    String phoneNumber;

    @NotBlank(message = "is required")
    AddressRequestDto address;
}
