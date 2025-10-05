package com.example.cosmocats.dto.product;

import com.example.cosmocats.dto.validation.StartsWithUpperCase;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Data
@Schema(description = "ProductRequest DTO class for creating/updating with some validation.")
public class ProductRequest {

    @NotBlank(message = "Name field is required.")
    @Size(min = 3, max = 20, message = "Name size must be between 3 and 20 characters long.")
    @StartsWithUpperCase(message = "Name must start with upper case.")
    @Schema(description = "Name must be between 3 and 20 characters long.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Description field is required.")
    @Size(min = 10, max = 100, message = "Description size must be between 10 and 100 characters long.")
    @StartsWithUpperCase(message = "Description must start with upper case.")
    @Schema(description = "Description must be between 10 and 100 characters long.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;

    @DecimalMin(value = "1", message = "Product price must be at least 1.")
    @Schema(description = "Price must be at least 1 inclusive.",
            minimum = "1.0",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private double price;
}
