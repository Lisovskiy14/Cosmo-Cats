package com.example.cosmocats.service.repository.iml;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.service.repository.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ProductRepositoryImpl implements ProductRepository {
    private final Map<UUID, Product> products = new ConcurrentHashMap<>();

    @Override
    public Product getProductById(UUID id) {
        return products.get(id);
    }

    @Override
    public Product saveProduct(Product product) {
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public void deleteProductById(UUID id) {
        products.remove(id);
    }

    @Override
    public List<Product> getAllProducts() {
        return products.values().stream().toList();
    }

    @Override
    public boolean existsById(UUID id) {
        return products.containsKey(id);
    }
}
