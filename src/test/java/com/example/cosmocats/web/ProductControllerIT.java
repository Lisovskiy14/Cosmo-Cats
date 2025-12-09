package com.example.cosmocats.web;

import com.example.cosmocats.AbstractIT;
import com.example.cosmocats.domain.Category;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.dto.product.UpdateProductRequestDto;
import com.example.cosmocats.repository.ProductRepository;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.client.WireMock;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
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
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ProductControllerIT extends AbstractIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoSpyBean
    private ProductService productService;

    @MockitoSpyBean
    private ProductRepository productRepository;

    @Autowired
    private CategoryService categoryService;


    @BeforeEach
    public void setup() {
        reset(productService);
    }

    @Test
    @Order(1)
    @SneakyThrows
    @DisplayName("Should Create Product Test")
    public void shouldCreateProduct() {
        Category category = Category.builder()
                .name("Category 1")
                .slug("category-1")
                .build();
        categoryService.saveCategory(category);
        ProductRequestDto productRequestDto = buildProductRequestDto("Galaxy product", BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value(productRequestDto.getName()))
                .andExpect(jsonPath("$.description").value(productRequestDto.getDescription()))
                .andExpect(jsonPath("$.price").value(productRequestDto.getPrice()));
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
    @Order(2)
    @SneakyThrows
    @DisplayName("Should Return Conflict On Product Name Response")
    public void shouldReturnConflictOnProductIdResponse() {
        ProductRequestDto productRequestDto = buildProductRequestDto("Galaxy product", BigDecimal.valueOf(100));

        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.type").value("urn:problem-type:conflict-error"))
                .andExpect(jsonPath("$.title").value("Resource Already Exists Exception"))
                .andExpect(jsonPath("$.status").value("409"))
                .andExpect(jsonPath("$.detail").exists())
                .andExpect(jsonPath("$.instance").value("/api/v1/products"));
    }

    @Test
    @Order(2)
    @SneakyThrows
    @DisplayName("Should Get All Products Test")
    public void shouldGetAllProducts() {
        mockMvc.perform(get("/api/v1/products")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products").isArray())
                .andExpect(jsonPath("$.products[*].id").exists())
                .andExpect(jsonPath("$.products[*].name").exists())
                .andExpect(jsonPath("$.products[*].description").exists())
                .andExpect(jsonPath("$.products[*].categoryName").exists())
                .andExpect(jsonPath("$.products[*].price").exists());
    }

    @Test
    @Order(2)
    @SneakyThrows
    @DisplayName("Should Get Product By Id Test")
    public void shouldGetProductById() {
        UUID productId = productService.getAllProducts().getFirst().getId();

        mockMvc.perform(get("/api/v1/products/{id}", productId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.name").exists())
                .andExpect(jsonPath("$.description").exists())
                .andExpect(jsonPath("$.categoryName").exists())
                .andExpect(jsonPath("$.price").exists());
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
    @Order(2)
    @SneakyThrows
    @DisplayName("Should Update Product Test")
    public void shouldUpdateProduct() {
        UUID productId = productService.getAllProducts().getFirst().getId();

        UpdateProductRequestDto updateProductRequestDto = UpdateProductRequestDto.builder()
                .price(BigDecimal.valueOf(400))
                .build();

        mockMvc.perform(put("/api/v1/products/{id}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateProductRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(productId.toString()))
                .andExpect(jsonPath("$.price").value(updateProductRequestDto.getPrice()));
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