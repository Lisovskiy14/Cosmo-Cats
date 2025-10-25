package com.example.cosmocats.domain;

import lombok.Value;

import java.util.List;
import java.util.UUID;

@Value
public class Cart {
    UUID id;
    List<Product> products;
}
