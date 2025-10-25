package com.example.cosmocats.service;

import com.example.cosmocats.domain.Product;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductById(UUID id);
    Product saveProduct(Product product);
    Product updateProduct(UUID id, Product product);
    void deleteProductById(UUID id);
}
