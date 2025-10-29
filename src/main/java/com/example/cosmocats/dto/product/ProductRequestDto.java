package com.example.cosmocats.dto.product;

import com.example.cosmocats.dto.validation.CosmicWordCheck;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class ProductRequestDto {

    @NotBlank(message = "is required")
    @Size(min = 3, max = 20, message = "must be between 3 and 20 characters long")
    @CosmicWordCheck
    String name;

    @NotBlank(message = "is required")
    @Size(min = 10, max = 100, message = "must be between 10 and 100 characters long")
    String description;

    @DecimalMin(value = "1", message = "must be at least 1")
    double price;
}
