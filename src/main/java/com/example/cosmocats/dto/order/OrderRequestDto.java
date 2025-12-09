package com.example.cosmocats.dto.order;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
public class OrderRequestDto {

    @NotBlank(message = "is required")
    @Size(min = 36, max = 36, message = "must be a valid UUID")
    String customerId;

    @NotBlank(message = "is required")
    @Size(min = 1, message = "must be at least 1 item")
    List<OrderItemRequestDto> items;
}
