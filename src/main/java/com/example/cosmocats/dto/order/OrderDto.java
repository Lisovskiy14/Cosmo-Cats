package com.example.cosmocats.dto.order;

import lombok.Builder;
import lombok.Value;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.util.List;

@Value
@Builder
@Jacksonized
public class OrderDto {
    String id;
    String customerId;
    BigDecimal totalPrice;
    String status;
    String createdAt;
    List<OrderItemDto> items;
}
