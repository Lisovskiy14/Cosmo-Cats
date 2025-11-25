package com.example.cosmocats.service.impl;

import com.example.cosmocats.domain.Category;
import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.UpdateProductRequestDto;
import com.example.cosmocats.repository.ProductRepository;
import com.example.cosmocats.repository.entity.CategoryEntity;
import com.example.cosmocats.repository.entity.ProductEntity;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.service.ProductService;
import com.example.cosmocats.service.exception.conflict.ProductNameAlreadyExistsException;
import com.example.cosmocats.service.exception.notFound.ProductNotFoundException;
import com.example.cosmocats.service.mapper.CategoryEntityMapper;
import com.example.cosmocats.service.mapper.ProductEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;
    private final CategoryService categoryService;
    private final CategoryEntityMapper categoryEntityMapper;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProducts() {
        return productRepository.findAll().stream()
                .map(productEntityMapper::toProduct)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(UUID id) {
        ProductEntity productEntity = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id.toString()));
        return productEntityMapper.toProduct(productEntity);
    }

    @Override
    @Transactional
    public Product createProduct(ProductRequestDto productRequestDto) {
        CategoryEntity categoryEntity = categoryEntityMapper.toCategoryEntity(
                categoryService.getCategoryByName(productRequestDto.getCategoryName()));

        ProductEntity productEntity = ProductEntity.builder()
                .name(productRequestDto.getName())
                .description(productRequestDto.getDescription())
                .category(categoryEntity)
                .price(productRequestDto.getPrice())
                .build();

        ProductEntity savedProductEntity = null;
        try {
            savedProductEntity = productRepository.saveAndFlush(productEntity);
        } catch (DataIntegrityViolationException ex) {
            throw new ProductNameAlreadyExistsException(
                    productEntity.getName(),
                    productEntity.getCategory().getId().toString()
            );
        }

        log.info("New product with id '{}' has been saved", savedProductEntity.getId());
        return productEntityMapper.toProduct(savedProductEntity);
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, UpdateProductRequestDto updateProductRequestDto) {
        ProductEntity productEntity = productEntityMapper.toProductEntity(getProductById(id));

        if (updateProductRequestDto.getName() != null) {
            productEntity.setName(updateProductRequestDto.getName());
        } else if (updateProductRequestDto.getDescription() != null) {
            productEntity.setDescription(updateProductRequestDto.getDescription());
        } else if (updateProductRequestDto.getCategoryName() != null) {
            Category category = categoryService.getCategoryByName(updateProductRequestDto.getCategoryName());
            productEntity.setCategory(categoryEntityMapper.toCategoryEntity(category));
        } else if (updateProductRequestDto.getPrice() != null) {
            productEntity.setPrice(updateProductRequestDto.getPrice());
        }

        ProductEntity updatedProductEntity = productRepository.save(productEntity);
        log.info("Product with id '{}' has been updated", id);
        return productEntityMapper.toProduct(updatedProductEntity);
    }

    @Override
    @Transactional
    public void deleteProductById(UUID id) {
        productRepository.deleteById(id);
        log.info("Product with id '{}' has been deleted", id);
    }
}
