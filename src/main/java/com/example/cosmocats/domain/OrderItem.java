package com.example.cosmocats.domain;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class OrderItem {
    UUID id;
    Order order;
    Product product;
    int quantity;
}
