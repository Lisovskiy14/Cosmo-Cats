package com.example.cosmocats.service;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.repository.ProductRepository;
import com.example.cosmocats.repository.entity.CategoryEntity;
import com.example.cosmocats.repository.entity.ProductEntity;
import com.example.cosmocats.service.exception.notFound.ProductNotFoundException;
import com.example.cosmocats.service.impl.ProductServiceImpl;
import com.example.cosmocats.service.mapper.CategoryEntityMapperImpl;
import com.example.cosmocats.service.mapper.ProductEntityMapperImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ProductServiceImpl.class, ProductEntityMapperImpl.class, CategoryEntityMapperImpl.class})
@DisplayName("Product Service Tests")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;

    @MockitoBean
    private ProductRepository productRepository;

    @MockitoBean
    private CategoryService categoryService;

    @Captor
    private ArgumentCaptor<ProductEntity> productArgumentCaptor;


    public static Stream<ProductRequestDto> provideProductRequests() {
        return Stream.of(
                buildProductRequest("Product 1", "Some description 1", BigDecimal.valueOf(100)),
                buildProductRequest("Product 2", "Some description 2", BigDecimal.valueOf(200)),
                buildProductRequest("Product 3", "Some description 3", BigDecimal.valueOf(300)),
                buildProductRequest("Product 4", "Some description 4", BigDecimal.valueOf(400))
        );
    }

    @ParameterizedTest
    @MethodSource("provideProductRequests")
    @DisplayName("Parameterized Save Product Test")
    public void shouldSaveProduct(ProductRequestDto productRequestDto) {
        when(productRepository.saveAndFlush(any(ProductEntity.class)))
                .thenReturn(ProductEntity.builder()
                        .id(UUID.randomUUID())
                        .name(productRequestDto.getName())
                        .description(productRequestDto.getDescription())
                        .price(productRequestDto.getPrice())
                        .category(CategoryEntity.builder()
                                .id(UUID.randomUUID())
                                .name(productRequestDto.getCategoryName())
                                .build())
                        .build());

        Product savedProduct = productService.createProduct(productRequestDto);

        verify(productRepository, times(1)).saveAndFlush(any(ProductEntity.class));
        assertThatNoException().isThrownBy(() -> productService.createProduct(productRequestDto));

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(productRequestDto.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(productRequestDto.getDescription());
        assertThat(savedProduct.getCategory()).isNotNull();
        assertThat(savedProduct.getPrice()).isEqualTo(productRequestDto.getPrice());
    }

    @Test
    @DisplayName("Get All Products Test")
    public void shouldGetAllProducts() {
        List<ProductEntity> productEntities = List.of(
                ProductEntity.builder()
                        .id(UUID.randomUUID())
                        .name("Product 1")
                        .description("Some description 1")
                        .price(BigDecimal.valueOf(100))
                        .build(),
                ProductEntity.builder()
                        .id(UUID.randomUUID())
                        .name("Product 2")
                        .description("Some description 2")
                        .price(BigDecimal.valueOf(200))
                        .build()
        );
        when(productRepository.findAll()).thenReturn(productEntities);

        List<Product> foundProducts = productService.getAllProducts();

        verify(productRepository, times(1)).findAll();
        assertThat(foundProducts).isNotNull();
        assertEquals(productEntities.size(), foundProducts.size());
    }

    @Test
    @DisplayName("Should Throw ProductNotFoundException")
    public void shouldThrowProductNotFoundException() {
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(UUID.randomUUID()));
    }

    @Test
    @DisplayName("Delete Products Test")
    public void shouldDeleteProducts() {
        UUID productId = UUID.randomUUID();

        assertThatNoException().isThrownBy(() -> productService.deleteProductById(productId));
        verify(productRepository, times(1)).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("Delete Product By Wrong ID Test")
    public void shouldNotThrowExceptionOnDeleteProductByWrongId() {
        assertThatNoException().isThrownBy(() -> productService.deleteProductById(UUID.randomUUID()));
    }

    private static ProductRequestDto buildProductRequest(String name, String description, BigDecimal price) {
        return ProductRequestDto.builder()
                .name(name)
                .description(description)
                .categoryName("Category 1")
                .price(price)
                .build();
    }
}