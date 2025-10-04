package com.example.cosmocats.dto.product;

import lombok.Value;

import java.util.List;

@Value
public class ProductListDTO {
    List<ProductDTO> products;
}
