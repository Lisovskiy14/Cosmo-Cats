package com.example.cosmocats.mapper;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequest;
import com.example.cosmocats.dto.product.ProductDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toDto(Product product);
    Product toEntity(ProductRequest productRequest);
}
