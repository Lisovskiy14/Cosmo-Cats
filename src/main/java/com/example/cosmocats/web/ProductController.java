package com.example.cosmocats.web;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequest;
import com.example.cosmocats.dto.product.ProductDTO;
import com.example.cosmocats.dto.product.ProductListDTO;
import com.example.cosmocats.mapper.ProductMapper;
import com.example.cosmocats.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
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
                                .map(productMapper::toDto)
                                .collect(Collectors.toList()))
                );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper
                        .toDto(productService.getProductById(id))
                );
    }

    @PostMapping
    public ResponseEntity<ProductDTO> saveProduct(@Valid @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.status(201)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper.toDto(savedProduct));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody ProductRequest productRequest) {
        Product product = productMapper.toEntity(productRequest);
        Product updatedProduct = productService.updateProduct(id, product);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productMapper.toDto(updatedProduct));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProductById(@PathVariable Long id) {
        productService.deleteProductById(id);
        return ResponseEntity.noContent()
                .build();
    }
}
