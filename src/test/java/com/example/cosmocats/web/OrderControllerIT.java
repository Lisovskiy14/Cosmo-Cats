package com.example.cosmocats.web;

import com.example.cosmocats.AbstractIT;
import com.example.cosmocats.domain.Category;
import com.example.cosmocats.dto.customer.AddressRequestDto;
import com.example.cosmocats.dto.customer.CustomerRequestDto;
import com.example.cosmocats.dto.order.OrderItemRequestDto;
import com.example.cosmocats.dto.order.OrderRequestDto;
import com.example.cosmocats.dto.order.UpdateOrderStatusRequestDto;
import com.example.cosmocats.dto.product.ProductRequestDto;
import com.example.cosmocats.service.CategoryService;
import com.example.cosmocats.service.CustomerService;
import com.example.cosmocats.service.OrderService;
import com.example.cosmocats.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import net.datafaker.Faker;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("OrderController IT")
@Tag("order-service")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderControllerIT extends AbstractIT {
    private static final Faker faker = new Faker();

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoSpyBean
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @BeforeEach
    public void setup() {
        reset(orderService);
    }

    @Test
    @Order(1)
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Place Order")
    public void shouldPlaceOrder() {
        Category category = Category.builder()
                .name("Category 1")
                .slug("category-1")
                .build();
        categoryService.saveCategory(category);

        UUID customerId = customerService.createCustomer(buildCustomerRequestDto()).getId();
        UUID productId = productService.createProduct(
                buildProductRequestDto("Galaxy Product", BigDecimal.valueOf(200))).getId();

        OrderRequestDto orderRequestDto = OrderRequestDto.builder()
                .customerId(customerId.toString())
                .items(List.of(
                        OrderItemRequestDto.builder()
                                .productId(productId.toString())
                                .quantity(2)
                                .build()
                ))
                .build();

        mockMvc.perform(post("/api/v1/orders")
                        .contentType(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(orderRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.orderNumber").exists())
                .andExpect(jsonPath("$.customerId").value(customerId.toString()))
                .andExpect(jsonPath("$.totalPrice").value(400))
                .andExpect(jsonPath("$.status").value("CREATED"))
                .andExpect(jsonPath("$.items[0].productId").value(productId.toString()))
                .andExpect(jsonPath("$.items[0].quantity").value(2));
    }

    @Test
    @Order(2)
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Get All Orders")
    public void shouldGetAllOrders() {
        mockMvc.perform(get("/api/v1/orders")
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray())
                .andExpect(jsonPath("$.orders[*].orderNumber").exists())
                .andExpect(jsonPath("$.orders[*].customerId").exists())
                .andExpect(jsonPath("$.orders[*].totalPrice").exists())
                .andExpect(jsonPath("$.orders[*].status").exists())
                .andExpect(jsonPath("$.orders[*].items").exists());
    }

    @Test
    @Order(2)
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Update Order Status")
    public void shouldUpdateOrderStatus() {
        String orderNumber = orderService.getAllOrders().getFirst().getOrderNumber();
        UpdateOrderStatusRequestDto updateOrderStatusRequestDto = new UpdateOrderStatusRequestDto("PAID");

        mockMvc.perform(patch("/api/v1/orders/{orderNumber}/status", orderNumber)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(updateOrderStatusRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
    }

    @Test
    @Order(2)
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Return Order By Order Number")
    public void shouldGetOrderByOrderNumber() {
        String orderNumber = orderService.getAllOrders().getFirst().getOrderNumber();

        mockMvc.perform(get("/api/v1/orders/{orderNumber}", orderNumber)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderNumber").value(orderNumber));
    }

    @Test
    @SneakyThrows
    @WithMockUser(roles = "ADMIN")
    @DisplayName("Should Return Validation Error")
    public void shouldReturnValidationError() {
        String incorrectOrderNumber = "incorrect-order-number";

        mockMvc.perform(get("/api/v1/orders/{orderNumber}", incorrectOrderNumber)
                        .contentType(APPLICATION_JSON_VALUE)
                        .accept(APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.type").value("urn:problem-type:validation-error"))
                .andExpect(jsonPath("$.title").value("Failed Validation Exception"))
                .andExpect(jsonPath("$.status").value("400"))
                .andExpect(jsonPath("$.detail").value("Request validation failed"))
                .andExpect(jsonPath("$.validationErrors").exists())
                .andExpect(jsonPath("$.validationErrors[0].field").value("orderNumber"))
                .andExpect(jsonPath("$.validationErrors[0].message").value("Order number must follow format ORD-YY-XXXXXX (e.g. ORD-25-YT3B9Z)"))
                .andExpect(jsonPath("$.instance").value("/api/v1/orders/" + incorrectOrderNumber));
    }

    private CustomerRequestDto buildCustomerRequestDto() {
        return CustomerRequestDto.builder()
                .firstName(faker.name().firstName())
                .lastName(faker.name().lastName())
                .email(faker.internet().emailAddress())
                .phoneNumber(faker.numerify("0#########"))
                .address(
                        AddressRequestDto.builder()
                                .country(faker.address().country())
                                .city(faker.address().city())
                                .street(faker.address().streetName())
                                .house(faker.address().buildingNumber())
                                .apartment(faker.numerify("##"))
                                .build()
                )
                .build();
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
