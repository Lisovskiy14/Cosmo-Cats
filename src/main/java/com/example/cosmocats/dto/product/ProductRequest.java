package com.example.cosmocats.dto.product;

import com.example.cosmocats.dto.validation.CosmicWordCheck;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
public class ProductRequest {

    @NotBlank(message = "Name field is required.")
    @Size(min = 3, max = 20, message = "Name size must be between 3 and 20 characters long.")
    @CosmicWordCheck
    private String name;

    @NotBlank(message = "Description field is required.")
    @Size(min = 10, max = 100, message = "Description size must be between 10 and 100 characters long.")
    private String description;

    @DecimalMin(value = "1", message = "Product price must be at least 1.")
    private double price;
}
