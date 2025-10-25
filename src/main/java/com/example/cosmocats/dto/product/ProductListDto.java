package com.example.cosmocats.dto.product;

import lombok.Value;

import java.util.List;

@Value
public class ProductListDto {
    List<ProductDto> products;
}
