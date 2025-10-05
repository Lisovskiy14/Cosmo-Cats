package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.service.ProductService;
import com.example.cosmocats.service.exception.NoSuchResourceException;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ProductServiceImpl implements ProductService {
    private final Map<Long, Product> products = new HashMap<>();
    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public List<Product> getAllProducts() {
        return List.copyOf(products.values());
    }

    @Override
    public Product getProductById(Long id) {
        checkForResourceById(id);
        return products.get(id);
    }

    @Override
    public Product saveProduct(Product product) {
        product.setId(counter.get());
        products.put(counter.get(), product);
        counter.incrementAndGet();
        return product;
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        checkForResourceById(id);
        product.setId(id);
        products.put(id, product);
        return product;
    }

    @Override
    public void deleteProductById(Long id) {
        checkForResourceById(id);
        products.remove(id);
    }

    private void checkForResourceById(Long id) {
        if (!products.containsKey(id)) {
            throw new NoSuchResourceException("Product with id " + id + " not found.");
        }
    }
}
