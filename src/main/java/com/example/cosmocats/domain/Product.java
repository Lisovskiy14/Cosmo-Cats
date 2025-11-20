package com.example.cosmocats.domain;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.util.UUID;

@Value
@Builder
public class Product {
    UUID id;
    String name;
    String description;
    Category category;
    BigDecimal price;
}
