package com.example.cosmocats.domain;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class Cart {
    private UUID id;
    private List<Product> products;
}
