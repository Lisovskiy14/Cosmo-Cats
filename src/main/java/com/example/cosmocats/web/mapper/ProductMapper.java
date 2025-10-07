package com.example.cosmocats.web.mapper;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequest;
import com.example.cosmocats.dto.product.ProductDTO;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDTO toProductDto(Product product);
    Product toProduct(ProductRequest productRequest);
}
