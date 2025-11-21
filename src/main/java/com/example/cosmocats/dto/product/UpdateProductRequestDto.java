package com.example.cosmocats.dto.product;

import com.example.cosmocats.dto.validation.cosmicWord.CosmicWordCheck;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.Value;

import java.math.BigDecimal;

@Value
public class UpdateProductRequestDto {

    @Size(min = 3, max = 20, message = "must be between 3 and 20 characters long")
    @CosmicWordCheck
    String name;

    @Size(min = 10, max = 100, message = "must be between 10 and 100 characters long")
    String description;

    @Size(min = 3, max = 20, message = "must be between 2 and 20 characters long")
    String categoryName;

    @DecimalMin(value = "1", message = "must be at least 1")
    BigDecimal price;
}
