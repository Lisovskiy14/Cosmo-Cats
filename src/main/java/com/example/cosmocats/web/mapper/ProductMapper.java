package com.example.cosmocats.web.mapper;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.ProductDto;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toProductDto(Product product);
    Product toProduct(ProductRequestDto productRequestDto);
}
