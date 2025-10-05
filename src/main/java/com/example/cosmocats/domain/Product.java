package com.example.cosmocats.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class Product {
    private Long id;
    private String name;
    private String description;
    private double price;
}
