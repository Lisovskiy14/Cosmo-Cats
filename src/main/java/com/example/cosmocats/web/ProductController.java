package com.example.cosmocats.web;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.ProductDto;
import com.example.cosmocats.dto.product.ProductListDto;
import com.example.cosmocats.dto.product.UpdateProductRequestDto;
import com.example.cosmocats.web.mapper.ProductWebMapper;
import com.example.cosmocats.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
    private final ProductWebMapper productWebMapper;

    @GetMapping
    public ResponseEntity<ProductListDto> getAllProducts() {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(new ProductListDto(
                        productService.getAllProducts().stream()
                                .map(productWebMapper::toProductDto)
                                .collect(Collectors.toList()))
                );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable UUID productId) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productWebMapper
                        .toProductDto(productService.getProductById(productId))
                );
    }

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(@Valid @RequestBody ProductRequestDto productRequestDto) {
        Product savedProduct = productService.createProduct(productRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .contentType(MediaType.APPLICATION_JSON)
                .body(productWebMapper.toProductDto(savedProduct));
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable UUID productId,
            @Valid @RequestBody UpdateProductRequestDto updateProductRequestDto
    ) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(productWebMapper.toProductDto(
                        productService.updateProduct(productId, updateProductRequestDto)));
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<Void> deleteProductById(@PathVariable UUID productId) {
        productService.deleteProductById(productId);
        return ResponseEntity.noContent()
                .build();
    }
}
