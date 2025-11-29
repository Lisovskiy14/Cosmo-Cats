package com.example.cosmocats.web;

import com.example.cosmocats.AbstractIT;
import com.example.cosmocats.domain.Category;
import com.example.cosmocats.domain.Product;
import com.example.cosmocats.dto.category.CategoryRequestDto;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.UpdateProductRequestDto;
import com.example.cosmocats.repository.ProductRepository;
import com.example.cosmocats.repository.entity.CategoryEntity;
import com.example.cosmocats.repository.entity.ProductEntity;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.service.ProductService;
import com.example.cosmocats.service.mapper.ProductEntityMapper;
import com.example.cosmocats.web.mapper.ProductWebMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@DisplayName("ProductController IT")
@Tag("product-service")
public class ProductControllerIT extends AbstractIT {

    private final ProductRequestDto PRODUCT_REQUEST_DTO = buildProductRequestDto("Galaxy product", BigDecimal.valueOf(100));

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductWebMapper productWebMapper;

    @Autowired
    private ProductEntityMapper productEntityMapper;

    @MockitoSpyBean
    private ProductService productService;

    @MockitoSpyBean
    private CategoryService categoryService;

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
        doReturn(Category.builder()
                .id(UUID.randomUUID())
                .name("Category 1")
                .slug("category-1")
                .build()).when(categoryService).getCategoryByName(any());

        doReturn(ProductEntity.builder()
                .id(UUID.randomUUID())
                .name(PRODUCT_REQUEST_DTO.getName())
                .description(PRODUCT_REQUEST_DTO.getDescription())
                .price(PRODUCT_REQUEST_DTO.getPrice())
                .build()).when(productRepository).saveAndFlush(any());

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
                        .content(objectMapper.writeValueAsString(buildProductRequestDto("wrong product", BigDecimal.valueOf(100)))))
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
    @DisplayName("Should Return Conflict On Product Name Response")
    public void shouldReturnConflictOnProductIdResponse() {
        doThrow(DataIntegrityViolationException.class).when(productRepository).saveAndFlush(any());
        doReturn(Category.builder()
                .id(UUID.randomUUID())
                .name("Category 1")
                .slug("category-1")
                .build()).when(categoryService).getCategoryByName(any());

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(PRODUCT_REQUEST_DTO)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("urn:problem-type:conflict-error"))
                .andExpect(jsonPath("$.title").value("Resource Already Exists Exception"))
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value("/api/v1/products"));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Get All Products Test")
    public void shouldGetAllProducts() {
        Product product = productWebMapper.toProduct(PRODUCT_REQUEST_DTO);
        product = product.toBuilder()
                .id(UUID.randomUUID())
                .build();

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
        Product product = productWebMapper.toProduct(PRODUCT_REQUEST_DTO);
        product = product.toBuilder()
                .id(UUID.randomUUID())
                .build();

        doReturn(Optional.of(productEntityMapper.toProductEntity(product)))
                .when(productRepository).findById(product.getId());

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
                .andExpect(jsonPath("$.title").value("Resource Not Found Exception"))
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
        UpdateProductRequestDto productRequestDto = UpdateProductRequestDto.builder()
                .price(BigDecimal.valueOf(400))
                .build();

        Product productBefore = Product.builder()
                .id(id)
                .name("Galaxy Product")
                .description("Some product description")
                .price(BigDecimal.valueOf(200))
                .build();
        doReturn(productBefore).when(productService).getProductById(id);

        ProductEntity productEntityAfter = productEntityMapper.toProductEntity(
                productBefore.toBuilder()
                        .price(productRequestDto.getPrice())
                        .build());
        doReturn(productEntityAfter).when(productRepository).save(any());

        mockMvc.perform(put("/api/v1/products/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.name").value(productBefore.getName()))
                .andExpect(jsonPath("$.description").value(productBefore.getDescription()))
                .andExpect(jsonPath("$.price").value(productRequestDto.getPrice()));
    }

    @Test
    @SneakyThrows
    @DisplayName("Should Delete Product Test")
    public void shouldDeleteProduct() {
        stubFor(WireMock.post("/payment-service/api/v1/payments")
                .willReturn(aResponse().withStatus(200)
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withBody("Some success payment response body")));

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

    private ProductRequestDto buildProductRequestDto(String name, BigDecimal price) {
        return ProductRequestDto.builder()
                .name(name)
                .description("Some product description")
                .categoryName("Category 1")
                .price(price)
                .build();
    }
}