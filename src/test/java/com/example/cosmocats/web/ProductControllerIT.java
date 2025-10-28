package com.example.cosmocats.web;

import com.example.cosmocats.AbstractIT;
import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.service.ProductService;
import com.example.cosmocats.service.repository.ProductRepository;
import com.example.cosmocats.web.mapper.ProductMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("ProductController IT")
@Tag("product-service")
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIT extends AbstractIT {

    private final ProductRequestDto PRODUCT_REQUEST_DTO = buildProductRequestDto("Galaxy product", 100);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductMapper productMapper;

    @MockitoSpyBean
    private ProductService productService;

    @MockitoSpyBean
    private ProductRepository productRepository;


    @BeforeEach
    public void setup() {
        reset(productService);
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Create Product Test")
    public void shouldCreateProduct() {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PRODUCT_REQUEST_DTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(PRODUCT_REQUEST_DTO.getName()))
                .andExpect(jsonPath("$.description").value(PRODUCT_REQUEST_DTO.getDescription()))
                .andExpect(jsonPath("$.price").value(PRODUCT_REQUEST_DTO.getPrice()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Return 400 Validation Error Response")
    public void shouldThrowValidationException() {
        mockMvc.perform(post("/api/v1/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(buildProductRequestDto("wrong product", 100))))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:problem-type:validation-error"))
                .andExpect(jsonPath("$.title").value("Failed Validation Exception"))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.detail").value("Request validation failed"))
                .andExpect(jsonPath("$.instance").value("/api/v1/products"))
                .andExpect(jsonPath("$.validationErrors").exists());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Return Conflict On Product Id Response")
    public void shouldReturnConflictOnProductIdResponse() {
        doReturn(true).when(productRepository).existsById(any());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PRODUCT_REQUEST_DTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("urn:problem-type:conflict-error"))
                .andExpect(jsonPath("$.title").value("Product Id Already Exists Exception"))
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value("/api/v1/products"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Get All Products Test")
    public void shouldGetAllProducts() {
        Product product = productMapper.toProduct(PRODUCT_REQUEST_DTO);
        product.setId(UUID.randomUUID());

        when(productService.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/v1/products")
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[0].id").exists())
                .andExpect(jsonPath("$.products[0].name").value(PRODUCT_REQUEST_DTO.getName()))
                .andExpect(jsonPath("$.products[0].description").value(PRODUCT_REQUEST_DTO.getDescription()))
                .andExpect(jsonPath("$.products[0].price").value(PRODUCT_REQUEST_DTO.getPrice()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Get Product By Id Test")
    public void shouldGetProductById() {
        Product product = productMapper.toProduct(PRODUCT_REQUEST_DTO);
        product.setId(UUID.randomUUID());

        doReturn(true).when(productRepository).existsById(product.getId());
        doReturn(product).when(productRepository).getProductById(product.getId());

        mockMvc.perform(get("/api/v1/products/{id}", product.getId())
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(product.getId().toString()))
                .andExpect(jsonPath("$.name").value(product.getName()))
                .andExpect(jsonPath("$.description").value(product.getDescription()))
                .andExpect(jsonPath("$.price").value(product.getPrice()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Return Product Not Found Response")
    public void shouldThrowProductNotFoundException() {
        UUID id = UUID.randomUUID();

        mockMvc.perform(get("/api/v1/products/{id}", id)
                    .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.type").value("urn:problem-type:not-found"))
                .andExpect(jsonPath("$.title").value("Product Not Found Exception"))
                .andExpect(jsonPath("$.status").value("404"))
                .andExpect(jsonPath("$.detail").value(
                        String.format("Product with id '%s' not found",  id)))
                .andExpect(jsonPath("$.instance").value(
                        String.format("/api/v1/products/%s", id)));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Update Product Test")
    public void shouldUpdateProduct() {
        UUID id = UUID.randomUUID();
        ProductRequestDto productRequestDto = buildProductRequestDto("Super Galaxy product", 200);

        doReturn(true).when(productRepository).existsById(id);

        mockMvc.perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(productRequestDto.getName()))
                .andExpect(jsonPath("$.description").value(productRequestDto.getDescription()))
                .andExpect(jsonPath("$.price").value(productRequestDto.getPrice()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Delete Product Test")
    public void shouldDeleteProduct() {
        mockMvc.perform(delete("/api/v1/products/{id}", UUID.randomUUID())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Return 500 Response")
    public void shouldReturnInternalServerErrorResponse() {
        UUID id = UUID.randomUUID();
        doThrow(new RuntimeException("Some internal exception")).when(productService).getProductById(any());

        mockMvc.perform(get("/api/v1/products/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.type").value("urn:problem-type:internal-server-error"))
                .andExpect(jsonPath("$.title").value("Internal Server Error"))
                .andExpect(jsonPath("$.status").value("500"))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value(
                        String.format("/api/v1/products/%s", id)));
    }

    private ProductRequestDto buildProductRequestDto(String name, double price) {
        return ProductRequestDto.builder()
                .name(name)
                .description("Some product description")
                .price(price)
                .build();
    }
}