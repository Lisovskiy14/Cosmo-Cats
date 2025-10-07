package com.example.cosmocats.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class Product {
    private UUID id;
    private String name;
    private String description;
    private double price;
}
