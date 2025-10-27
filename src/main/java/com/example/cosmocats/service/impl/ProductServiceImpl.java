package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.service.ProductService;
import com.example.cosmocats.service.exception.ProductNotFoundException;
import com.example.cosmocats.service.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.getAllProducts();
    }

    @Override
    public Product getProductById(UUID id) {
        checkForProductById(id);
        log.info("Product with id {} has been found", id);
        return productRepository.getProductById(id);
    }

    @Override
    public Product saveProduct(Product product) {
        UUID id = UUID.randomUUID();
        product.setId(id);
        productRepository.saveProduct(product);
        log.info("New product with id {} has been saved", id);
        return product;
    }

    @Override
    public Product updateProduct(UUID id, Product product) {
        checkForProductById(id);
        product.setId(id);
        productRepository.saveProduct(product);
        log.info("Product with id {} has been updated", id);
        return product;
    }

    @Override
    public void deleteProductById(UUID id) {
        productRepository.deleteProductById(id);
        log.info("Product with id {} has been deleted", id);
    }

    private void checkForProductById(UUID id) {
        if (!productRepository.existsById(id)) {
            throw new ProductNotFoundException(id.toString());
        }
    }
}
