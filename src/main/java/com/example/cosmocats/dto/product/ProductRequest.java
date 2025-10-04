package com.example.cosmocats.dto.product;

import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class ProductRequest {

    @NotBlank(message = "Product name is required.")
    @Size(min = 3, max = 20, message = "Product name size must be between 3 and 20 characters long.")
    private String name;

    @NotBlank(message = "Product description is required.")
    @Size(min = 10, max = 100, message = "Product description size must be between 10 and 100 characters long.")
    private String description;

    @DecimalMin(value = "0.0", inclusive = false, message = "Product price must be greater than 0.")
    private double price;
}
