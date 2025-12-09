package com.example.cosmocats.dto.customer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class AddressRequestDto {

    @NotBlank(message = "is required")
    @Size(min = 2, max = 30, message = "must be between 2 and 30 characters long")
    String country;

    @NotBlank(message = "is required")
    @Size(min = 2, max = 30, message = "must be between 2 and 30 characters long")
    String city;

    @NotBlank(message = "is required")
    @Size(min = 2, max = 30, message = "must be between 2 and 30 characters long")
    String street;

    @NotBlank(message = "is required")
    @Size(min = 1, max = 5, message = "must be between 1 and 5 characters long")
    String house;

    @NotBlank(message = "is required")
    @Size(min = 1, max = 5, message = "must be between 1 and 5 characters long")
    String apartment;
}
