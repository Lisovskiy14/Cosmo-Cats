package com.example.cosmocats.domain;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class Order {
    private Long id;
    private List<Product> products = new ArrayList<>();
    private double totalPrice;
}
