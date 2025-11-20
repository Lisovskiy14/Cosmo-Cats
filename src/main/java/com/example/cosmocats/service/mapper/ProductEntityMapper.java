package com.example.cosmocats.service.mapper;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.repository.entity.ProductEntity;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductEntityMapper {
    ProductEntity toProductEntity(Product product);
    Product toProduct(ProductEntity productEntity);
}
