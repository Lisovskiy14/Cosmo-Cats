package com.example.cosmocats.web;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequest;
import com.example.cosmocats.dto.product.ProductDTO;
import com.example.cosmocats.dto.product.ProductListDTO;
import com.example.cosmocats.web.mapper.ProductMapper;
import com.example.cosmocats.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    private final ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<ProductListDTO> getAllProducts() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductListDTO(
                        productService.getAllProducts().stream()
                                .map(productMapper::toProductDto)
                                .collect(Collectors.toList()))
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper
                        .toProductDto(productService.getProductById(id))
                );
    }

    @PostMapping
    public ResponseEntity<ProductDTO> saveProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper.toProductDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toProduct(productRequest);
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper.toProductDto(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
