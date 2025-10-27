package com.example.cosmocats.service.repository;

import com.example.cosmocats.domain.Product;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ProductRepository {
    Product getProductById(UUID id);
    Product saveProduct(Product product);
    void deleteProductById(UUID id);
    List<Product> getAllProducts();
    boolean existsById(UUID id);
}
