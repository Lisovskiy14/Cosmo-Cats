package com.example.cosmocats.dto.order;

import jakarta.validation.constraints.NotBlank;
import lombok.Value;

@Value
public class UpdateOrderStatusRequestDto {

    @NotBlank(message = "is required")
    String status;
}
