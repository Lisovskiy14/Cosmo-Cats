package com.example.cosmocats.web;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.ProductDto;
import com.example.cosmocats.dto.product.ProductListDto;
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
    public ResponseEntity<ProductListDto> getAllProducts() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductListDto(
                        productService.getAllProducts().stream()
                                .map(productMapper::toProductDto)
                                .collect(Collectors.toList()))
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper
                        .toProductDto(productService.getProductById(id))
                );
    }

    @PostMapping
    public ResponseEntity<ProductDto> saveProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        Product product = productMapper.toProduct(productRequestDto);
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper.toProductDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(@PathVariable UUID id, @Valid @RequestBody ProductRequestDto productRequestDto) {
        Product product = productMapper.toProduct(productRequestDto);
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
