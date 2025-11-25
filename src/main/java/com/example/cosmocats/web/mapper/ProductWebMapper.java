package com.example.cosmocats.web.mapper;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.ProductDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ProductWebMapper {
    @Mapping(source = "category.name", target = "categoryName")
    ProductDto toProductDto(Product product);
    Product toProduct(ProductRequestDto productRequestDto);
}
