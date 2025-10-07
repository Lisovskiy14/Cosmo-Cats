package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.service.ProductService;
import com.example.cosmocats.service.exception.ProductNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final Map<UUID, Product> products = new ConcurrentHashMap<>();

    @Override
    public List<Product> getAllProducts() {
        return List.copyOf(products.values());
    }

    @Override
    public Product getProductById(UUID id) {
        checkForResourceById(id);
        log.info("Product with id {} has been found", id);
        return products.get(id);
    }

    @Override
    public Product saveProduct(Product product) {
        UUID id = UUID.randomUUID();
        product.setId(id);
        products.put(id, product);
        log.info("New product with id {} has been saved", id);
        return product;
    }

    @Override
    public Product updateProduct(UUID id, Product product) {
        checkForResourceById(id);
        product.setId(id);
        products.put(id, product);
        log.info("Product with id {} has been updated", id);
        return product;
    }

    @Override
    public void deleteProductById(UUID id) {
        products.remove(id);
        log.info("Product with id {} has been deleted", id);
    }

    private void checkForResourceById(UUID id) {
        if (!products.containsKey(id)) {
            throw new ProductNotFoundException(id.toString());
        }
    }
}
