package com.example.cosmocats.service;

import com.example.cosmocats.domain.Product;

import java.util.List;

public interface ProductService {

    List<Product> getAllProducts();
    Product getProductById(Long id);
    Product saveProduct(Product product);
    Product updateProduct(Long id, Product product);
    void deleteProductById(Long id);
}
