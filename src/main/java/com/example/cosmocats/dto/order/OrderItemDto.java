package com.example.cosmocats.dto.order;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

@Value
@Builder
@Jacksonized
public class OrderItemDto {
    String productId;
    int quantity;
}
