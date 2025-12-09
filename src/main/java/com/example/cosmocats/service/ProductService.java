package com.example.cosmocats.service;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.UpdateProductRequestDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductById(UUID id);
    Product createProduct(ProductRequestDto productRequestDto);
    Product updateProduct(UUID id, UpdateProductRequestDto updateProductRequestDto);
    void deleteProductById(UUID id);
}
