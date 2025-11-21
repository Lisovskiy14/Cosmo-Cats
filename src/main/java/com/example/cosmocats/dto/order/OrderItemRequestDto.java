package com.example.cosmocats.dto.order;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Value;

@Value
public class OrderItemRequestDto {

    @NotBlank(message = "is required")
    @Size(min = 36, max = 36, message = "must be a valid UUID")
    String productId;

    @Min(value = 1, message = "must be at least 1")
    int quantity;
}
