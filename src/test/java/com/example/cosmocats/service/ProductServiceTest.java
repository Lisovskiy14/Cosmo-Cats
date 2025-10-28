package com.example.cosmocats.service;

import com.example.cosmocats.domain.Product;
import com.example.cosmocats.service.exception.ProductNotFoundException;
import com.example.cosmocats.service.impl.ProductServiceImpl;
import com.example.cosmocats.service.repository.ProductRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = {ProductServiceImpl.class})
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DisplayName("Product Service Tests")
public class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @MockitoBean
    private ProductRepository productRepository;

    @Captor
    private ArgumentCaptor<Product> productArgumentCaptor;


    public static Stream<Product> provideProducts() {
        return Stream.of(
                buildProduct("Product 1", "Some description 1", 100),
                buildProduct("Product 2", "Some description 2", 200),
                buildProduct("Product 3", "Some description 3", 300),
                buildProduct("Product 4", "Some description 4", 400)
        );
    }

    @ParameterizedTest
    @MethodSource("provideProducts")
    @Order(1)
    @DisplayName("Parameterized Save Product Test")
    public void shouldSaveProduct(Product product) {
        when(productRepository.saveProduct(productArgumentCaptor.capture()))
                .thenAnswer(inv -> inv.getArgument(0));

        Product savedProduct = productService.saveProduct(product);

        verify(productRepository, times(1)).saveProduct(any(Product.class));
        assertThatNoException().isThrownBy(() -> productService.saveProduct(product));

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isEqualTo(product.getId());
        assertThat(savedProduct.getName()).isEqualTo(product.getName());
        assertThat(savedProduct.getDescription()).isEqualTo(product.getDescription());
        assertThat(savedProduct.getPrice()).isEqualTo(product.getPrice());
    }

    @Test
    @Order(2)
    @DisplayName("Get All Products Test")
    public void shouldGetAllProducts() {
        List<Product> products = provideProducts().toList();
        when(productRepository.getAllProducts()).thenReturn(products);

        List<Product> foundProducts = productService.getAllProducts();

        verify(productRepository, times(1)).getAllProducts();
        assertThat(foundProducts).isNotNull();
        assertEquals(products.size(), foundProducts.size());
    }

    @Test
    @Order(3)
    @DisplayName("Should Throw ProductNotFoundException")
    public void shouldThrowProductNotFoundException() {
        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(UUID.randomUUID()));
    }

    @Test
    @Order(4)
    @DisplayName("Delete Products Test")
    public void shouldDeleteProducts() {
        List<UUID> productIds = provideProducts().map(Product::getId).toList();

        productIds.forEach(id -> {productService.deleteProductById(id);});
        int postLength = productService.getAllProducts().size();

        assertEquals(0, postLength);
    }

    @Test
    @Order(5)
    @DisplayName("Delete Product By Wrong ID Test")
    public void shouldNotThrowExceptionOnDeleteProductByWrongId() {
        assertThatNoException().isThrownBy(() -> productService.deleteProductById(UUID.randomUUID()));
    }

    private static Product buildProduct(String name, String description, double price) {
        return Product.builder()
                .id(UUID.randomUUID())
                .name(name)
                .description(description)
                .price(price)
                .build();
    }
}
